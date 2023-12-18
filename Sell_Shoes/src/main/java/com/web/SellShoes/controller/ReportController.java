package com.web.SellShoes.controller;

import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.SellShoes.dto.responseDto.OrderRevenueResponseDto;
import com.web.SellShoes.entity.Account;
import com.web.SellShoes.entity.Category;
import com.web.SellShoes.entity.OrderDetail;
import com.web.SellShoes.entity.Variant;
import com.web.SellShoes.service.AccountService;
import com.web.SellShoes.service.CategoryService;
import com.web.SellShoes.service.OrderDetailService;
import com.web.SellShoes.service.OrderService;
import com.web.SellShoes.service.VariantService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/report")
public class ReportController {
	private final OrderService orderService;
	private final AccountService accountService;
	private final OrderDetailService orderDetailService;
	private final VariantService variantService;
	private final CategoryService categoryService;

	@GetMapping()
	public String viewReport(Model model, HttpSession session) {
		model.addAttribute("fullName",(String) session.getAttribute("fullName"));
		Double totalRevenue = orderService.calculateTotalRevenue();
		model.addAttribute("totalRevenue", totalRevenue);

		//totalQuantitySold
		List<OrderDetail> orderDetails = orderDetailService.getAllOrderDetails();
		int totalQuantitySold = 0;
		for (OrderDetail orderDetail : orderDetails) {
			int quantitySold = orderDetail.getQuantity();

			totalQuantitySold += quantitySold;
		}
		model.addAttribute("totalQuantitySold", totalQuantitySold);
		
		//totalAccount
		int totalQuantityAccount = accountService.getAccountUser().size();
		model.addAttribute("totalQuantityAccount", totalQuantityAccount);
		
		//quantityProduct
		int quantityProduct = 0;
		List<Variant> variants = variantService.getVariants();
		for (Variant variant : variants) {
			quantityProduct += variant.getQuantity();
		}
		model.addAttribute("quantityProduct", quantityProduct);

		return "/admin/report/report";
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

	@GetMapping("/soldOfCategory")
	@ResponseBody
	public ResponseEntity<Map<String, Integer>> getQuantityOfProductAndCategory() {
		
		Map<String,Integer> soleOfCatehory = new HashMap<String,Integer>();
		List<Category> categories = categoryService.getAll();
		for (Category category : categories) {
			soleOfCatehory.put(category.getCategoryName(),orderDetailService.getQuantityOfProductAndCategory(category));
		}
		
		return ResponseEntity.ok(soleOfCatehory);
	}

}
