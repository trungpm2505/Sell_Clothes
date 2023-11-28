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
import com.web.SellShoes.entity.Account;
import com.web.SellShoes.entity.Cart;
import com.web.SellShoes.entity.Order;
import com.web.SellShoes.entity.OrderDetail;
import com.web.SellShoes.entity.Promotion;
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
	public String view() {
		return "shop/shopcontent/checkout";
	}

	Optional<Cart> cart;
	CartResponseDto cartResponseDto;

	@GetMapping(value = "/checkout")
	public String createOrder(@RequestParam List<Integer> cartIdList, Model model, HttpSession session) {
		String email = (String) session.getAttribute("email");
		Optional<Account> account = accountService.findUserByEmail("trungpmpd05907@fpt.edu.vn");

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

		return "shop/shopcontent/checkout";
	}

	@PostMapping(value = "/addOrder")
	@ResponseBody
	public ResponseEntity<?> addOrder(@Valid @RequestBody OrderRequestDto orderRequestDto, BindingResult bindingResult,
			HttpSession session) {
		Map<String, Object> errors = new HashMap<>();
		String email = (String) session.getAttribute("email");
		Optional<Account> account = accountService.findUserByEmail("trungpmpd05907@fpt.edu.vn");
		// Lấy thông tin khuyến mãi từ mã giảm giá trong đơn hàng
		// Optional<Promotion> promotion =
		// promotionService.getPromotionById(orderRequestDto.getPromotion_id());
		// Kiểm tra và xử lý lỗi từ việc validate dữ liệu đầu vào
		// Optional<Promotion> promotion ;
		if (bindingResult.hasErrors()) {
			errors.put("bindingErrors", bindingResult.getAllErrors());
			return ResponseEntity.badRequest().body(errors);
		}

		// Tạo một đơn hàng mới và set thông tin từ orderRequestDto
		Order order = new Order();
		
//		String address = orderRequestDto.getAddress() + ", " +  orderRequestDto.getProvince()
//	  + ", " +  orderRequestDto.getDistrict() + ", " +  orderRequestDto.getWard();
		order.setAdrress(orderRequestDto.getAddress());

		
		order.setFullName(orderRequestDto.getFullName());
		order.setPhone_Number(orderRequestDto.getPhone_Number());
		order.setNote(orderRequestDto.getNote());
		order.setAccount(account.get());
		// order.setPromotion(promotion.get());
		order.setTotalMoney(0);
		
  		Promotion promotion = promotionService.getPromotionById(orderRequestDto.getPromotionId()).orElse(null);
		System.out.println(promotion + "===");

		order.setPromotion(promotion);
		
		orderService.save(order);
		float totalPayment = 0;
		// Lặp qua danh sách các sản phẩm trong đơn hàng để tạo thông tin chi tiết đơn
		// hàng
		for (Integer id : orderRequestDto.getCartIds()) {
			System.out.println(id + "jasdkjo");
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
//            totalPayment += orderDetail.getTotalMoney();

		}
		// Cập nhật tổng giá trị đơn hàng
		order.setTotalMoney(orderRequestDto.getConfirmedPrice());

		orderService.save(order);

		return ResponseEntity.ok().body("Đặt hàng thành công!");
	}

	@GetMapping("/applyPromotionByCode")
	public ResponseEntity<Optional<Promotion>> applyPromotionByCode(@RequestParam("coupon_code") String couponCode) {
		// Thực hiện xử lý của bạn ở đây
		Optional<Promotion> appliedPromotion = promotionService.getPromotionByCouponCode(couponCode);
		return ResponseEntity.ok(appliedPromotion);
	}

}
