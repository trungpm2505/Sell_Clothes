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
@RequestMapping(value="/userOrder")
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

//@GetMapping(value = "/checkout")
//public ResponseEntity<List<CartResponseDto>> createOrder(@RequestParam List<Integer> cartIds, HttpSession session) {
//    String email = (String) session.getAttribute("email");
//    Optional<Account> account = accountService.findUserByEmail("trungpmpd05907@fpt.edu.vn");
//
//    AccountResponseDto accountResponseDto = mapper.accountToAccountResponseDto(account.get());
//    List<CartResponseDto> cartResponseDtos = new ArrayList<>();
//
//    for (Integer id : cartIds) {
//        cart = cartService.getCartById(id);
//        cartResponseDto = mapper.cartToCartResponseDto(cart.get());
//        cartResponseDtos.add(cartResponseDto);
//    }
//
//    return ResponseEntity.ok().body(cartResponseDtos);
//}
Optional<Cart>cart;
CartResponseDto cartResponseDto;
@GetMapping(value = "/checkout")
public String createOrder(@RequestParam List<Integer> cartIdList,Model model, HttpSession session) {
    String email = (String) session.getAttribute("email");
    Optional<Account> account = accountService.findUserByEmail("trungpmpd05907@fpt.edu.vn");

    AccountResponseDto accountResponseDtos = mapper.accountToAccountResponseDto(account.get());
    List<CartResponseDto> cartResponseDtos = new ArrayList<>();

    for (Integer id : cartIdList) {
        cart = cartService.getCartById(id);
        cartResponseDto = mapper.cartToCartResponseDto(cart.get());
        cartResponseDtos.add(cartResponseDto);
    }
      model.addAttribute("cartResponseDtos",cartResponseDtos);
      model.addAttribute("accountResponseDto",accountResponseDtos);
      model.addAttribute("orderRequestDtos",new OrderRequestDto());

    return "shop/shopcontent/checkout";
}
@PostMapping(value="/addOrder")
@ResponseBody
public ResponseEntity<?>addOrder(@Valid @RequestBody OrderRequestDto orderRequestDto,
		BindingResult bindingResult,HttpSession session){
	Map<String,Object>errors=new HashMap<>();
	 String email = (String) session.getAttribute("email");
	  Optional<Account> account = accountService.findUserByEmail("trungpmpd05907@fpt.edu.vn");	 
     Optional<Promotion>promotion=promotionService.getPromotionByCouponCode(orderRequestDto.getPromotion().getCouponCode());
	  if (bindingResult.hasErrors()) {
		errors.put("bindingErrors", bindingResult.getAllErrors());
		return ResponseEntity.badRequest().body(errors);
	}
	  Order order=new Order();
	  order.setFullName(orderRequestDto.getFullName());
	  order.setAdrress(orderRequestDto.getAdrress());
	  order.setPhone_Number(orderRequestDto.getPhone_Number());
	  order.setNote(orderRequestDto.getNote());
	  order.setAccount(account.get());
	  order.setTotalMoney(0);
	  order.setPromotion(orderRequestDto.getPromotion());
	  
	  if (promotion.isPresent()) {
	        Promotion validPromotion = promotion.get();
	        float totalPayment = calculateTotalPayment(orderRequestDto.getCartIds());

	        if (validPromotion.getDiscountType() == 1) { // Kiểm tra loại giảm giá: 1 - Phần trăm, 2 - Số tiền
	            float discountAmount = (validPromotion.getDiscountValue() / 100) * totalPayment;
	            order.setTotalMoney(totalPayment - discountAmount);
	        } else if (validPromotion.getDiscountType() == 2) {
	            order.setTotalMoney(totalPayment - validPromotion.getDiscountValue());
	        }
	        
	        order.setPromotion(validPromotion);
	    } else {
	        float totalPayment = calculateTotalPayment(orderRequestDto.getCartIds());
	        order.setTotalMoney(totalPayment);
	    }
	  orderService.save(order);
	  float totalPayment=0;
	  for (Integer id : orderRequestDto.getCartIds()) {
		Optional<Cart>cart=cartService.getCartById(id);
		OrderDetail orderDetail=new OrderDetail();
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
 
		
		  

		orderDetailService.save(orderDetail);

		totalPayment += (orderDetail.getTotalMoney());
	}
	
	order.setTotalMoney(totalPayment);
	orderService.save(order);
	
	return ResponseEntity.ok().body("Order Success!");
}

private float calculateTotalPayment(List<Integer> cartIds) {
    float totalPayment = 0;
    for (Integer id : cartIds) {
        Optional<Cart> cartOptional = cartService.getCartById(id);
        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            float price = cart.getVariant().getCurrentPrice() != null ? cart.getVariant().getCurrentPrice() : cart.getVariant().getPrice();
            float itemTotal = price * cart.getQuantity();
            totalPayment += itemTotal;
        }
    }
    return totalPayment;
}

//Optional<Cart>cart;
//CartResponseDto cartResponseDto;
//@GetMapping(value = "/checkout")
//public ResponseEntity<List<CartResponseDto>> createOrder(@RequestParam List<Integer> cartIdList, HttpSession session) {
//    String email = (String) session.getAttribute("email");
//    Optional<Account> account = accountService.findUserByEmail("trungpmpd05907@fpt.edu.vn");
//
//    List<CartResponseDto> cartResponseDtos = new ArrayList<>();
//
//    for (Integer id : cartIdList) {
//        cart = cartService.getCartById(id);
//        CartResponseDto cartResponseDto = mapper.cartToCartResponseDto(cart.get());
//        cartResponseDtos.add(cartResponseDto);
//    }
//
//    return new ResponseEntity<>(cartResponseDtos, HttpStatus.OK);
//}

}
