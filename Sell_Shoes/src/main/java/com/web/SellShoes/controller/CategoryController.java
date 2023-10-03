package com.web.SellShoes.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.SellShoes.dto.responseDto.CategoryResponseDto;
import com.web.SellShoes.entity.Category;
import com.web.SellShoes.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/category")
public class CategoryController {
	private final CategoryService categoryService;
	
	@GetMapping()
	public String list(Model model) {

		return "admin/categories/add";
	}
	
	@GetMapping(value = "/getAll")
	public ResponseEntity<?> getAllCategory() {
		List<Category> categories = categoryService.getAll();
		
		List<CategoryResponseDto> categoryResponseDtos = categories.stream()
				.map(category -> new CategoryResponseDto(category.getId(),
						category.getCategoryName()))
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(categoryResponseDtos);
	}
}
