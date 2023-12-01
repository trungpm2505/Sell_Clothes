package com.web.SellShoes.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.web.SellShoes.dto.requestDto.VariantRequestDto;
import com.web.SellShoes.dto.responseDto.VariantPageResponseDto;
import com.web.SellShoes.dto.responseDto.VariantResponseDto;
import com.web.SellShoes.entity.Color;
import com.web.SellShoes.entity.Size;
import com.web.SellShoes.entity.Variant;
import com.web.SellShoes.mapper.Mapper;
import com.web.SellShoes.service.ColorService;
import com.web.SellShoes.service.SizeService;
import com.web.SellShoes.service.VariantService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/variant")
public class VariantController {
	private final Mapper mapper;
	private final VariantService variantService;
	private final SizeService sizeService;
	private final ColorService colorService;

	@PostMapping(value = "/add")
	@ResponseBody
	public ResponseEntity<?> saveVariant(@Valid @ModelAttribute VariantRequestDto variantRequestDto,
			BindingResult bindingResult) {

		Map<String, Object> errors = new HashMap<>();

		if (bindingResult.hasErrors()) {
			errors.put("bindingErrors", bindingResult.getAllErrors());
		}
		Optional<Variant> variantOptional = variantService.getVariantBySizeAndColor(variantRequestDto.getProduct(),
				variantRequestDto.getSize(), variantRequestDto.getColor());
		if (variantOptional.isPresent()) {
			errors.put("variantDuplicate",
					"Variant already exists in this size and color! please enter a new variant. ");
		}

		if (!errors.isEmpty()) {
			return ResponseEntity.badRequest().body(errors);
		}

		Variant variant = mapper.variantRequestDtoToVariant(variantRequestDto);
		variantService.save(variant);

		return ResponseEntity.ok().body("{\"message\": \"Add variant successfully\"}");
	}

	@PutMapping(value = "/update")
	@ResponseBody
	public ResponseEntity<?> updateVariant(@Valid @ModelAttribute VariantRequestDto variantRequestDto,
			BindingResult bindingResult) {
		Map<String, Object> errors = new HashMap<>();

		if (bindingResult.hasErrors()) {
			errors.put("bindingErrors", bindingResult.getAllErrors());
		}

		if (!errors.isEmpty()) {
			return ResponseEntity.badRequest().body(errors);
		}
		Variant variant = mapper.variantRequestDtoToVariant(variantRequestDto);
		variant.setUpdateAt(LocalDate.now());
		variantService.save(variant);

		return ResponseEntity.ok().body("{\"message\": \"Update variant successfully\"}");
	}

	@DeleteMapping(value = "/delete")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> deleteVariant(@RequestParam("variantId") Integer variantId) {

		// check if category is exist
		Optional<Variant> variantById = variantService.getVariantById(variantId);
		if (variantById.isEmpty() || variantById.get().getDeleteAt() != null) {
			return ResponseEntity.badRequest().body("variant is not exist! Delete failse!");
		}

		variantService.deleteVariant(variantById.get());

		return ResponseEntity.ok().body("Variant deleted successfully.");
	}

	@GetMapping(value = "/getVariantPage")
	@ResponseBody
	public ResponseEntity<VariantPageResponseDto> getVariantPage(@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(required = false, defaultValue = "0") Integer sizeId,
			@RequestParam(required = false, defaultValue = "0") Integer colorId,
			@RequestParam(required = false) String keyword) {

		Page<Variant> variantPage = null;
		Size size2 = null;
		Color color = null;

		if (sizeId != 0) {
			size2 = sizeService.getSize(sizeId).get();
		}
		if (colorId != 0) {
			color = colorService.getColor(colorId).get();
		}
		variantPage = variantService.searchVariant(page, size, size2, color, keyword);

		List<VariantResponseDto> variantResponseDtos = variantPage.stream()
				.map(variant -> new VariantResponseDto(variant.getId(), variant.getProduct().getId(),
						variant.getSize().getId(), variant.getColor().getId(), variant.getProduct().getTitle(),
						variant.getNote(), variant.getSize().getSize(), variant.getColor().getColor(),
						variant.getPrice(), variant.getCurrentPrice(), variant.getQuantity(), variant.getBuyCount(),
						variant.getCreateAt(), variant.getUpdateAt()))
				.collect(Collectors.toList());

		VariantPageResponseDto variantPageResponseDto = new VariantPageResponseDto(variantPage.getTotalPages(),
				variantPage.getNumber(), variantPage.getSize(), variantResponseDtos);
		return ResponseEntity.ok(variantPageResponseDto);

	}

	@GetMapping(value = "/getVariant")
	@ResponseBody
	public ResponseEntity<?> getVariant(@RequestParam Integer productId,
			@RequestParam(required = false, defaultValue = "0") Integer sizeId,
			@RequestParam(required = false, defaultValue = "0") Integer colorId) {

		List<Variant> variants = null;
		Size size = null;
		Color color = null;

		if (sizeId != 0) {
			size = sizeService.getSize(sizeId).get();
		}
		if (colorId != 0) {
			color = colorService.getColor(colorId).get();
		}
		variants = variantService.getVariantsByProductIdAndSizeIdColorId(productId, size, color);

		List<VariantResponseDto> variantResponseDtos = variants.stream()
				.map(variant -> new VariantResponseDto(variant.getId(), variant.getProduct().getId(),
						variant.getSize().getId(), variant.getColor().getId(), variant.getProduct().getTitle(),
						variant.getNote(), variant.getSize().getSize(), variant.getColor().getColor(),
						variant.getPrice(), variant.getCurrentPrice(), variant.getQuantity(), variant.getBuyCount(),
						variant.getCreateAt(), variant.getUpdateAt()))
				.collect(Collectors.toList());
		return ResponseEntity.ok(variantResponseDtos);
	}

}
