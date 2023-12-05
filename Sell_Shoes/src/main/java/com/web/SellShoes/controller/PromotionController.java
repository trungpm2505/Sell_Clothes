package com.web.SellShoes.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.SellShoes.dto.requestDto.PromotionRequestDto;
import com.web.SellShoes.dto.responseDto.PromotionPageResponseDto;
import com.web.SellShoes.dto.responseDto.PromotionResponseDto;
import com.web.SellShoes.entity.Promotion;
import com.web.SellShoes.mapper.Mapper;
import com.web.SellShoes.service.PromotionService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/promotion")
public class PromotionController {
	private final PromotionService promotionService;
	private final Mapper mapper;

	@GetMapping(value = "/admin")
	public String getPromotionView(HttpSession session,Model model) {
		model.addAttribute("fullName",(String) session.getAttribute("fullName"));
		return "admin/promotion/addOrList";
	}

	@PostMapping(value = "/add")
	@ResponseBody
	public ResponseEntity<?> savePromotion(@Valid @ModelAttribute PromotionRequestDto promotionRequestDto,
			BindingResult bindingResult) {

		Map<String, Object> errors = new HashMap<>();
		if (bindingResult.hasErrors()) {
			errors.put("bindingErrors", bindingResult.getAllErrors());
		}

		Optional<Promotion> promotionByCoupon = promotionService
				.getPromotionByCouponCode(promotionRequestDto.getCouponCode());
		if (promotionByCoupon.isPresent()) {
			errors.put("couponDuplicate", "Coupon Code already exists! please enter a new coupon code. ");
		}

		if (promotionRequestDto.getExpiredDate() != null
				&& promotionRequestDto.getExpiredDate().isBefore(LocalDate.now())) {
			errors.put("expiredDate", "Promotion period has ended. Please select a future expiration date.");
		}

		if (promotionRequestDto.getDiscountType() == 1) {
			if (promotionRequestDto.getDiscountValue() != null
					&& (promotionRequestDto.getDiscountValue() < 1 || promotionRequestDto.getDiscountValue() > 100)) {
				errors.put("discountValue", "Discount levels range from 1% - 100%. ");
			}
			if (promotionRequestDto.getMaxValue() == null) {
				errors.put("maxValue", "MaxValue cannot empty. ");
			}
			if (promotionRequestDto.getMaxValue() != null && promotionRequestDto.getMaxValue() < 1000) {
				errors.put("maxValue", "The discount must be greater than 1000. ");
			}
		} else {
			if (promotionRequestDto.getDiscountValue() != null && promotionRequestDto.getDiscountValue() < 1000) {
				errors.put("discountValue", "The discount must be greater than 1000. ");
			}
		}

		if (!errors.isEmpty()) {
			return ResponseEntity.badRequest().body(errors);
		}
		Promotion promotion = mapper.promotionRequestDtoToPromotion(promotionRequestDto);
		promotionService.save(promotion);

		return ResponseEntity.ok().body("{\"message\": \"Add promotion successfully\"}");
	}

	@PutMapping(value = "/update")
	@ResponseBody
	public ResponseEntity<?> updatePromotion(@Valid @ModelAttribute PromotionRequestDto promotionRequestDto,
			BindingResult bindingResult) {
		Map<String, Object> errors = new HashMap<>();

		if (bindingResult.hasErrors()) {
			errors.put("bindingErrors", bindingResult.getAllErrors());
		}
		System.out.println("promotionId: " + promotionRequestDto.getId());
		Optional<Promotion> promotionByCoupon = promotionService
				.getPromotionByCouponCode(promotionRequestDto.getCouponCode());

		if (promotionByCoupon.isPresent() && !promotionByCoupon.get().getId().equals(promotionRequestDto.getId())) {
			errors.put("couponDuplicate", "Coupon Code already exists! please enter a new coupon code. ");
		}

		if (promotionRequestDto.getExpiredDate() != null
				&& promotionRequestDto.getExpiredDate().isBefore(LocalDate.now())) {
			errors.put("expiredDate", "Promotion period has ended. Please select a future expiration date.");
		}

		if (promotionRequestDto.getDiscountType() == 1) {
			if (promotionRequestDto.getDiscountValue() != null
					&& (promotionRequestDto.getDiscountValue() < 1 || promotionRequestDto.getDiscountValue() > 100)) {
				errors.put("discountValue", "Discount levels range from 1% - 100%. ");
			}
			if (promotionRequestDto.getMaxValue() == null) {
				errors.put("maxValue", "MaxValue cannot empty. ");
			}
			if (promotionRequestDto.getMaxValue() != null && promotionRequestDto.getMaxValue() < 1000) {
				errors.put("maxValue", "The discount must be greater than 1000. ");
			}
		} else {
			if (promotionRequestDto.getDiscountValue() != null && promotionRequestDto.getDiscountValue() < 1000) {
				errors.put("discountValue", "The discount must be greater than 1000. ");
			}
		}

		if (!errors.isEmpty()) {
			return ResponseEntity.badRequest().body(errors);
		}
		Promotion promotion = mapper.promotionRequestDtoToPromotion(promotionRequestDto);
		promotion.setUpdateAt(LocalDate.now());
		promotionService.save(promotion);

		return ResponseEntity.ok().body("{\"message\": \"Update Promotion successfully\"}");
	}

	@DeleteMapping(value = "/delete")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> deleteProduct(@RequestParam("promotionId") Integer promotionId) {

		// check if category is exist
		Optional<Promotion> promotionById = promotionService.getPromotionById(promotionId);
		if (promotionById.isEmpty() || promotionById.get().getDeleteAt() != null) {

			return ResponseEntity.badRequest().body("Product is not exist! Delete failse!");
		}

		promotionService.deletePromotion(promotionById.get());

		return ResponseEntity.ok().body("Promotion deleted successfully.");
	}

	@GetMapping(value = "/getProductPage")
	@ResponseBody
	public ResponseEntity<PromotionPageResponseDto> getPromotionPage(@RequestParam(defaultValue = "8") int size,
			@RequestParam(defaultValue = "0") int page, @RequestParam(required = false) String keyword) {

		Page<Promotion> promotionPage = null;
		if (keyword == null || keyword.equals("")) {
			promotionPage = promotionService.getAllPromotion(page, size);
		} else {
			promotionPage = promotionService.getPromotionByKey(page, size, keyword);
		}

		List<PromotionResponseDto> promotionResponseDtos = promotionPage.stream()
				.map(promotion -> new PromotionResponseDto(promotion.getId(), promotion.getCouponCode(),
						promotion.getName(), promotion.getDiscountType(), promotion.getDiscountValue(),
						promotion.getMaximumDiscountValue(), promotion.isPublic(), promotion.isActive(),
						promotion.getExpiredAt(), promotion.getCreateAt(), promotion.getUpdateAt()))
				.collect(Collectors.toList());

		PromotionPageResponseDto promotionPageResponseDto = new PromotionPageResponseDto(promotionPage.getTotalPages(),
				promotionPage.getNumber(), promotionPage.getSize(), promotionResponseDtos);

		return ResponseEntity.ok(promotionPageResponseDto);
	}

}
