package com.web.SellShoes.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.SellShoes.dto.responseDto.ColorResponseDto;
import com.web.SellShoes.entity.Color;
import com.web.SellShoes.service.ColorService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/color")
public class ColorController {
	private final ColorService colorService;
	
	@GetMapping(value = "/getAll")
	public ResponseEntity<?> getAllColor() {
		List<Color> colors = colorService.getAll();
		
		List<ColorResponseDto> colorResponseDtos = colors.stream()
				.map(size -> new ColorResponseDto(size.getId(),
						size.getColor()))
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(colorResponseDtos);
	}
}
