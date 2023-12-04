package com.web.SellShoes.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.http.MediaType;
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

import com.web.SellShoes.dto.requestDto.CartRequestDto;
import com.web.SellShoes.dto.responseDto.CartResponseDto;
import com.web.SellShoes.entity.Account;
import com.web.SellShoes.entity.Cart;
import com.web.SellShoes.entity.Order;
import com.web.SellShoes.entity.OrderDetail;
import com.web.SellShoes.entity.Variant;
import com.web.SellShoes.mapper.Mapper;
import com.web.SellShoes.service.AccountService;
import com.web.SellShoes.service.CartService;
import com.web.SellShoes.service.OrderDetailService;
import com.web.SellShoes.service.OrderService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/cart")
public class CartController {
	private final CartService cartService;
	private final AccountService accountService;
	private final OrderService orderService;
	private final Mapper mapper;
	private final OrderDetailService orderDetailService;

	@GetMapping()
	public String view(HttpSession session, Model model) {
		
		model.addAttribute("fullName",(String) session.getAttribute("fullName"));
		return "/shop/shopcontent/cart";
	}

	@PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> save(@Valid @RequestBody CartRequestDto cartRequestDto, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			// return new
			// RedirectView("/product/details?productId="+cartRequestDto.getProductId());
		}

		Cart cart = mapper.cartRequestDtoToCart(cartRequestDto);
		cartService.save(cart);

		return ResponseEntity.ok().body("Add cart success!");

	}

	@GetMapping(value = "/findAll")
	public ResponseEntity<List<CartResponseDto>> findAllByUser(HttpSession session) {

		String email = (String) session.getAttribute("email");
		Optional<Account> account = accountService.findUserByEmail(email);

		List<Cart> findByUser = cartService.findCartByAccount(account.get());
		List<CartResponseDto> cartResponseDtos = new ArrayList<>();
		CartResponseDto cartResponseDto = null;

		for (Cart cart : findByUser) {
			cartResponseDto = mapper.cartToCartResponseDto(cart);

			cartResponseDtos.add(cartResponseDto);
		}

		return ResponseEntity.ok().body(cartResponseDtos);
	}

	@PostMapping(value = "/updateQuantity")
	@ResponseBody
	public ResponseEntity<String> updateQuantity(@RequestParam Integer cartId, @RequestParam Integer quantity) {
		Optional<Cart> cart = cartService.getCartById(cartId);
		if (cart.isEmpty()) {
			return ResponseEntity.badRequest().body("Sản phẩm này không tồn tại trong giỏ hàng của bạn!");
		}

		// Lấy biến thể tương ứng với mục trong giỏ hàng
		Variant variant = cart.get().getVariant();
		if (variant.getQuantity() < quantity) {
			return ResponseEntity.badRequest().body("Số lượng yêu cầu vượt quá số lượng tồn kho của biến thể!");
		}

		// Cập nhật số lượng trong giỏ hàng
		cart.get().setQuantity(quantity);
		cartService.save(cart.get());

		return ResponseEntity.ok("Cập nhật số lượng thành công");
	}

	@PostMapping(value = "/deleteCart")
	@ResponseBody
	public ResponseEntity<?> deleteCart(@RequestParam Integer cartId) {
		Optional<Cart> cart = cartService.getCartById(cartId);
		if (cart.isEmpty()) {
			return ResponseEntity.badRequest().body("This product does not exist in your cart!");
		}
		cartService.deleteCart(cart.get().getId());
		return ResponseEntity.ok("Cart deleted successfully");
	}

	@PostMapping(value = "/deleteAllCart")
	@ResponseBody
	public ResponseEntity<?> deleteAllCart() {
		cartService.deleteAllCart();
		return ResponseEntity.ok("All carts deleted successfully");
	}
	
	@PostMapping(value="/addByOrder" ,consumes = MediaType.APPLICATION_JSON_VALUE)
	//@ResponseBody
	public ResponseEntity<String> addByOrder(@RequestParam Integer orderId ) {
		
		Optional<Order> order=orderService.getOrderById(orderId);
		List<OrderDetail> orderDetailsList = orderDetailService.getOrderDtails(order.get());
		
		for (OrderDetail orderDetails : orderDetailsList) {
			CartRequestDto cartRequestDto= new CartRequestDto();
			cartRequestDto.setQuantity(orderDetails.getQuantity());
			cartRequestDto.setVariantId(orderDetails.getVariant().getId());
			Cart cart = mapper.cartRequestDtoToCart(cartRequestDto);
			cartService.save(cart);
		}
		
		return ResponseEntity.ok().body("Add cart success!");
	
	}

}
