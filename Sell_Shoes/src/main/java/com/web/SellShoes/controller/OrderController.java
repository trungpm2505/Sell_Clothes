package com.web.SellShoes.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.SellShoes.dto.responseDto.OrderDetailResponseDto;
import com.web.SellShoes.dto.responseDto.OrderPageResponseDto;
import com.web.SellShoes.dto.responseDto.OrderResponseDto;
import com.web.SellShoes.entity.Account;
import com.web.SellShoes.entity.Order;
import com.web.SellShoes.entity.OrderDetail;
import com.web.SellShoes.mapper.Mapper;
import com.web.SellShoes.service.AccountService;
import com.web.SellShoes.service.OrderDetailService;
import com.web.SellShoes.service.OrderService;
import com.web.SellShoes.service.RateService;
import com.web.SellShoes.service.VariantService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "order")
public class OrderController {
	private final OrderService orderService;
	private final RateService rateService;
	private final AccountService accountService;
	private final OrderDetailService orderDetailService;
	private final VariantService variantService;
	private final Mapper mapper;

	@GetMapping("/admin/all")
	public String viewOrder(HttpSession session ,Model model) {
		model.addAttribute("fullName",(String) session.getAttribute("fullName"));
		return "admin/order/list";
	}

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	LocalDate startDateParse = null;
	LocalDate endDateParse = null;
	LocalDate createDateParse = null;

	@GetMapping(value = "/getOrderPage")
	@ResponseBody
	public ResponseEntity<OrderPageResponseDto> getOrderPageForAdmin(@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "0") int status,
			@RequestParam(defaultValue = "false") boolean search, @RequestParam(required = false) String key,
			@RequestParam(required = false) String createAt, @RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate, @RequestParam(required = false) String ortherTime) {
		Page<Order> orderPage = null;

		if (status == 0) {
			if (search == false) {
				orderPage = orderService.getOrderPage(page, size);
			} else {
				if (createAt != "") {
					createDateParse = LocalDate.parse(createAt, formatter);

				} else if (startDate != "" || endDate != "") {

					if (startDate != "") {
						startDateParse = LocalDate.parse(startDate, formatter);
					}
					if (endDate != "") {
						endDateParse = LocalDate.parse(endDate, formatter);
					}
				}
				orderPage = orderService.findByDateRangeAndKey(page, size, createDateParse, startDateParse,
						endDateParse, key, status);
			}
		} else if (status != 0) {
			if (search == false) {
				orderPage = orderService.getOrderPageByStatus(page, size, status);
			} else {
				if (createAt != "") {
					createDateParse = LocalDate.parse(createAt, formatter);

				} else if (startDate != "" || endDate != "") {

					if (startDate != "") {
						startDateParse = LocalDate.parse(startDate, formatter);
					}
					if (endDate != "") {
						endDateParse = LocalDate.parse(endDate, formatter);
					}
				}
				orderPage = orderService.findByDateRangeAndKey(page, size, createDateParse, startDateParse,
						endDateParse, key, status);

			}

		}

		endDateParse = null;
		startDateParse = null;
		createDateParse = null;
		OrderPageResponseDto orderPageResponseDto = new OrderPageResponseDto();
		List<OrderResponseDto> orderResponseDtos = new ArrayList<>();
		if (orderPage != null) {
			orderResponseDtos = orderPage.stream()
					.map(order -> new OrderResponseDto(order.getId(), order.getFullName(), order.getPhone_Number(),
							order.getAdrress(), order.getOrder_date(), order.getTotalMoney(), order.getStatus(),
							order.getNote(), rateService.getRateByOrder(order).size() != 0 ? true : false))
					.collect(Collectors.toList());

			orderPageResponseDto.setTotalPages(orderPage.getTotalPages());
			orderPageResponseDto.setSize(orderPage.getSize());
			orderPageResponseDto.setOrderResponseDtos(orderResponseDtos);
			orderPageResponseDto.setCurrentPage(orderPage.getNumber());

		}
		return ResponseEntity.ok(orderPageResponseDto);
	}

	@PutMapping(value = "/updateStatus")
	public ResponseEntity<?> updateStatus(@RequestParam Integer orderId, @RequestParam int status,
			@RequestParam(defaultValue = "false") boolean undo) {

		Optional<Order> order = orderService.getOrderById(orderId);

		if (order.isEmpty()) {
			return ResponseEntity.badRequest().body("Order does not exist.");
		}

		orderService.updateOrderStatus(order.get(), status);

		if (status == 4) {
			order.get().setCompletedAt(LocalDate.now());
			orderService.save(order.get());
			List<OrderDetail> OrderDetailsList = orderDetailService.getOrderDtails(order.get());
			for (OrderDetail orderDetails : OrderDetailsList) {
				orderDetails.getVariant()
						.setQuantity(orderDetails.getVariant().getQuantity() - orderDetails.getQuantity());
				orderDetails.getVariant().setBuyCount(orderDetails.getQuantity());
				variantService.save(orderDetails.getVariant());
			}
		} else if (undo == true) {
			order.get().setCompletedAt(null);
			List<OrderDetail> OrderDetailsList = orderDetailService.getOrderDtails(order.get());
			for (OrderDetail orderDetails : OrderDetailsList) {
				orderDetails.getVariant()
						.setQuantity(orderDetails.getQuantity() + orderDetails.getVariant().getQuantity());
				orderDetails.getVariant()
						.setBuyCount(orderDetails.getVariant().getBuyCount() - orderDetails.getQuantity());
				variantService.save(orderDetails.getVariant());
			}
		}

		return ResponseEntity.noContent().build();

	}

	@GetMapping(value = "/user/getOrderPage")
	@ResponseBody
	public ResponseEntity<OrderPageResponseDto> getOrderPageForUser(@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "0") int status,
			@RequestParam(required = false) String keyWord, HttpSession session, Model model) {
		model.addAttribute("fullName",(String) session.getAttribute("fullName"));
		Page<Order> orderPage = null;
		// Authentication authentication =
		// SecurityContextHolder.getContext().getAuthentication();
		String email = (String) session.getAttribute("email");
		Optional<Account> account = accountService.findUserByEmail(email);

		if (keyWord == null || keyWord.equals("")) {
			if (status == 0) {
				orderPage = orderService.getOrderPageByAccount(page, size, account.get());
			} else if (status != 0) {
				orderPage = orderService.getOrderPageByStatusAndAccount(page, size, status, account.get());
			}
		} else {
			orderPage = orderService.getOrderPageByKeywordAndAccount(page, size, keyWord, account.get());
		}

		List<OrderResponseDto> orderResponseDtos = orderPage.stream()
				.map(order -> new OrderResponseDto(order.getId(), order.getFullName(), order.getPhone_Number(),
						order.getAdrress(), order.getOrder_date(), order.getTotalMoney(), order.getStatus(),
						order.getNote(), rateService.getRateByOrder(order).size() != 0 ? true : false))
				.collect(Collectors.toList());

		for (int i = 0; i < orderPage.getContent().size(); i++) {
			List<OrderDetail> OrderDetails = orderDetailService.getOrderDtails(orderPage.getContent().get(i));
			List<OrderDetailResponseDto> orderDetailsResponseDtos = mapper
					.listOrderDtailsToListOrderDetailsDto(OrderDetails);
			orderResponseDtos.get(i).setOrderDetaitsResponseDtos(orderDetailsResponseDtos);

		}

		OrderPageResponseDto orderPageResponseDto = new OrderPageResponseDto(orderPage.getTotalPages(),
				orderPage.getNumber(), orderPage.getSize(), orderResponseDtos);

		return ResponseEntity.ok(orderPageResponseDto);

	}

	@GetMapping("/user/all-order")
	public String getAllOrderForUser(HttpSession session,Model model) {
		model.addAttribute("fullName",(String) session.getAttribute("fullName"));
		return "shop/shopcontent/historyOrder";
	}
}
