package com.web.SellShoes.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.SellShoes.dto.responseDto.OrderPageResponseDto;
import com.web.SellShoes.dto.responseDto.OrderResponseDto;
import com.web.SellShoes.entity.Order;
import com.web.SellShoes.service.OrderDetailService;
import com.web.SellShoes.service.OrderService;
import com.web.SellShoes.service.RateService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "order")
public class OrderController {
	private final OrderService orderService;
	private final RateService rateService;
	// private final OrderDetailService orderDetailService;

	@GetMapping("/admin/all")
	public String viewOrder(Model model) {

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
		key = "";
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
}
