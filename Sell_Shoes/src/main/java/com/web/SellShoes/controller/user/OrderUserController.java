package com.web.SellShoes.controller.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.SellShoes.dto.requestDto.OrderRequestDto;
import com.web.SellShoes.dto.responseDto.AccountResponseDto;
import com.web.SellShoes.dto.responseDto.CartResponseDto;
import com.web.SellShoes.dto.responseDto.PromotionResponseDto;
import com.web.SellShoes.dto.responseDto.VariantResponseDto;
import com.web.SellShoes.entity.Account;
import com.web.SellShoes.entity.Cart;
import com.web.SellShoes.entity.Order;
import com.web.SellShoes.entity.OrderDetail;
import com.web.SellShoes.entity.Promotion;
import com.web.SellShoes.entity.Variant;
import com.web.SellShoes.mapper.Mapper;
import com.web.SellShoes.service.AccountService;
import com.web.SellShoes.service.CartService;
import com.web.SellShoes.service.OrderDetailService;
import com.web.SellShoes.service.OrderService;
import com.web.SellShoes.service.PromotionService;
import com.web.SellShoes.service.VariantService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/userOrder")
public class OrderUserController {
	private final OrderService orderService;
	private final AccountService accountService;
	private final OrderDetailService orderDetailService;
	private final CartService cartService;
	private final PromotionService promotionService;
	private final Mapper mapper;

	@GetMapping()
	public String view1(HttpSession session, Model model) {
		model.addAttribute("fullName", (String) session.getAttribute("fullName"));
		return "/shop/shopcontent/checkout";
	}

	Optional<Cart> cart;
	CartResponseDto cartResponseDto;

	@GetMapping(value = "/checkout")
	public String createOrder(@RequestParam List<Integer> cartIdList, Model model, HttpSession session) {
		model.addAttribute("fullName", (String) session.getAttribute("fullName"));
		String email = (String) session.getAttribute("email");
		Optional<Account> account = accountService.findUserByEmail(email);

		AccountResponseDto accountResponseDtos = mapper.accountToAccountResponseDto(account.get());
		List<CartResponseDto> cartResponseDtos = new ArrayList<>();

		Float total = (float) 0;
		for (Integer id : cartIdList) {
			cart = cartService.getCartById(id);
			cartResponseDto = mapper.cartToCartResponseDto(cart.get());
			total += cartResponseDto.getTotal();
			cartResponseDtos.add(cartResponseDto);
		}
		model.addAttribute("total", total);
		model.addAttribute("cartResponseDtos", cartResponseDtos);
		model.addAttribute("accountResponseDto", accountResponseDtos);
		model.addAttribute("orderRequestDtos", new OrderRequestDto());

		return "/shop/shopcontent/checkout";
	}

	@PostMapping(value = "/addOrder")
	@ResponseBody
	public ResponseEntity<?> addOrder(@Valid @RequestBody OrderRequestDto orderRequestDto, BindingResult bindingResult,
			HttpSession session) {
		Map<String, Object> errors = new HashMap<>();
		String email = (String) session.getAttribute("email");
		Optional<Account> account = accountService.findUserByEmail(email);
		if (bindingResult.hasErrors()) {
			errors.put("bindingErrors", bindingResult.getAllErrors());
			return ResponseEntity.badRequest().body(errors);
		}

		// Tạo một đơn hàng mới và set thông tin từ orderRequestDto
		Order order = new Order();
		String address = orderRequestDto.getProvince() + ", " + orderRequestDto.getDistrict() + ", "
				+ orderRequestDto.getWard() + ", " + orderRequestDto.getAddress();
		order.setAdrress(address);
		order.setFullName(orderRequestDto.getFullName());
		order.setPhone_Number(orderRequestDto.getPhone_Number());
		order.setNote(orderRequestDto.getNote());
		order.setAccount(account.get());
		order.setTotalMoney(0);

		Promotion promotion = promotionService.getPromotionById(orderRequestDto.getPromotionId()).orElse(null);
		if (promotion != null) {
			order.setPromotion(promotion);
		}

		orderService.save(order);
		float totalPayment = 0;
		// Lặp qua danh sách các sản phẩm trong đơn hàng để tạo thông tin chi tiết đơn
		// hàng
		for (Integer id : orderRequestDto.getCartIds()) {
			Optional<Cart> cart = cartService.getCartById(id);

			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setQuantity(cart.get().getQuantity());
			if (cart.get().getVariant().getCurrentPrice() != null) {
				orderDetail.setPrice(cart.get().getVariant().getCurrentPrice());
				orderDetail.setTotalMoney(cart.get().getQuantity() * cart.get().getVariant().getCurrentPrice());
			} else {
				orderDetail.setPrice(cart.get().getVariant().getPrice());
				orderDetail.setTotalMoney(cart.get().getQuantity() * cart.get().getVariant().getPrice());
			}
			orderDetail.setVariant(cart.get().getVariant());
			orderDetail.setOrder(order);
			orderDetail.setCurentPrice(cart.get().getVariant().getCurrentPrice());

			// Lưu thông tin chi tiết đơn hàng vào cơ sở dữ liệu
			orderDetailService.save(orderDetail);
			totalPayment += orderDetail.getTotalMoney();

		}
		// Cập nhật tổng giá trị đơn hàng
		if (orderRequestDto.getConfirmedPrice() != null && orderRequestDto.getConfirmedPrice() > 0) {
			order.setTotalMoney(orderRequestDto.getConfirmedPrice()); // Sử dụng giá đã xác nhận sau khi giảm giá
		} else {
			order.setTotalMoney(totalPayment); // Sử dụng tổng thanh toán từ các mục giỏ hàng
		}
		orderService.save(order);
		return ResponseEntity.ok().body("Đặt hàng thành công!");
	}

	@GetMapping("/applyPromotionByCode")
	public ResponseEntity<?> applyPromotionByCode(@RequestParam("coupon_code") String couponCode) {
		Map<String, Object> errors = new HashMap<>();
		if (couponCode.isEmpty()) {
			errors.put("couponCode", "Coupon code is empty");
			return ResponseEntity.badRequest().body(errors);
		}

		Optional<Promotion> optionalPromotion = promotionService.getPromotionByCouponCode(couponCode);
		if (optionalPromotion.isPresent()) {
			Promotion promotion = optionalPromotion.get();
			PromotionResponseDto promotionResponseDto = promotionToPromotionResponese(promotion);
			return ResponseEntity.ok(promotionResponseDto);
		} else {
			errors.put("couponCode", "Promotion not found for the provided coupon code");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
		}
	}

	public PromotionResponseDto promotionToPromotionResponese(Promotion promotion) {

		PromotionResponseDto promotionResponseDto = new PromotionResponseDto();

		promotionResponseDto.setId(promotion.getId());
		promotionResponseDto.setCouponCode(promotion.getCouponCode());
		promotionResponseDto.setCreateAt(promotion.getCreateAt());
		promotionResponseDto.setDiscountType(promotion.getDiscountType());
		promotionResponseDto.setDiscountValue(promotion.getDiscountValue());
		promotionResponseDto.setExpiredDate(promotion.getExpiredAt());
		promotionResponseDto.setMaxValue(promotion.getMaximumDiscountValue());
		promotionResponseDto.setName(promotion.getName());
		promotionResponseDto.setUpdateAt(promotion.getUpdateAt());
		return promotionResponseDto;
	}

	@GetMapping("/order-success")
	public String view(HttpSession session, Model model) {
		model.addAttribute("fullName", (String) session.getAttribute("fullName"));
		return "/shop/shopcontent/order-success";
	}

}
