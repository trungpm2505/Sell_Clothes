package com.web.SellShoes.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.SellShoes.dto.responseDto.OrderDetailResponseDto;
import com.web.SellShoes.dto.responseDto.OrderResponseDto;
import com.web.SellShoes.entity.Order;
import com.web.SellShoes.entity.OrderDetail;
import com.web.SellShoes.mapper.Mapper;
import com.web.SellShoes.service.OrderDetailService;
import com.web.SellShoes.service.OrderService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/orderDetails")
public class OrderDetailController {
	private final OrderService orderService;
	private final OrderDetailService orderDetailService;
	private final Mapper mapper;
	
	@GetMapping("/get")
	public String viewOrderDetail(@RequestParam Integer orderId,HttpSession session,Model model) {
		model.addAttribute("fullName",(String) session.getAttribute("fullName"));
		Optional<Order> order = orderService.getOrderById(orderId);

		OrderResponseDto orderResponseDto = mapper.orderToOrderResponseDto(order.get());

		List<OrderDetail> orderDetails = order.get().getOrderDetailsSet();

		List<OrderDetailResponseDto> orderDetailsResponseDtos = mapper
				.listOrderDtailsToListOrderDetailsDto(orderDetails);
		int number = orderDetailService.getNumberOfProductInOrder(order.get());

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
		model.addAttribute("number", number);
		//model.addAttribute("userName", userService.findUserByEmail(authentication.getName()).get().getUserName());
		model.addAttribute("orderResponseDto", orderResponseDto);
		model.addAttribute("orderDetailsResponseDtos", orderDetailsResponseDtos);
		
		
		if (roles.contains("ADMIN")) {
			return "admin/order/orderDetail";
		} else {
			return "shop/shopcontent/orderDetail";
		}
		// 
		
	}
	
	
}
