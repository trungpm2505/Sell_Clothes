package com.web.SellShoes.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.SellShoes.dto.responseDto.BrandResponseDto;
import com.web.SellShoes.entity.Brand;
import com.web.SellShoes.service.BrandService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/brand")
public class BrandController {
	private final BrandService brandService;
	
	@GetMapping(value = "/getAll")
	public ResponseEntity<?> getAllBrand() {
		List<Brand> brands = brandService.getAll();
		
		List<BrandResponseDto> brandResponseDtos = brands.stream()
				.map(brand -> new BrandResponseDto(brand.getId(),
						brand.getName()))
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(brandResponseDtos);
	}
}
