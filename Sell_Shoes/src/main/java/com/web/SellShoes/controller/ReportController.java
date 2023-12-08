package com.web.SellShoes.controller;

import java.time.Year;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.SellShoes.dto.responseDto.OrderRevenueResponseDto;
import com.web.SellShoes.entity.OrderDetail;
import com.web.SellShoes.service.AccountService;
import com.web.SellShoes.service.OrderDetailService;
import com.web.SellShoes.service.OrderService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/report")
public class ReportController {
	private final OrderService orderService;
	private final AccountService accountService;
	private final OrderDetailService orderDetailService;

	@GetMapping("/totalRevenue")
	public ResponseEntity<?> getTotalRevenue() {
	    try {
	        double totalRevenue = orderService.calculateTotalRevenue();
	        String formattedTotalRevenue = String.format("%.2f", totalRevenue); 
	        return ResponseEntity.ok(formattedTotalRevenue);
	    } catch (Exception e) {
	        return ResponseEntity.status(500).body("Error fetching total revenue data.");
	    }
	}


	@GetMapping("/revenueByMonth")
	public ResponseEntity<?> getRevenueByMonth(@RequestParam(required = false) Integer year) {
	    try {
	        if (year == null) {
	            year = Year.now().getValue();
	        }

	        List<OrderRevenueResponseDto> revenueByMonth = orderService.calculateRevenueByMonth(year);
	        return ResponseEntity.ok(revenueByMonth);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).body("Error fetching revenue data by month.");
	    }
	}
	
	@GetMapping("/totalQuantitySold")
	public ResponseEntity<?> getTotalQuantitySold() {
	    try {
	        List<OrderDetail> orderDetails = orderDetailService.getAllOrderDetails();

	        int totalQuantitySold = 0;

	        for (OrderDetail orderDetail : orderDetails) {
	            int quantitySold = orderDetail.getQuantity();

	            totalQuantitySold += quantitySold;
	        }

	        return ResponseEntity.ok(totalQuantitySold);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).body("Error fetching total quantity sold data.");
	    }
	}


	
	
}
