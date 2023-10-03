package com.web.SellShoes.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.SellShoes.dto.responseDto.SizeResponseDto;
import com.web.SellShoes.entity.Size;
import com.web.SellShoes.service.SizeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/size")
public class SizeController {
	private final SizeService sizeService;
	
	
	
	@GetMapping(value = "/getAll")
	public ResponseEntity<?> getAllSize() {
		List<Size> sizes = sizeService.getAll();
		
		List<SizeResponseDto> sizeResponseDtos = sizes.stream()
				.map(size -> new SizeResponseDto(size.getId(),
						size.getSize()))
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(sizeResponseDtos);
	}
}
