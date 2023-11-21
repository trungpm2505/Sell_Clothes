package com.web.SellShoes.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.web.SellShoes.dto.responseDto.VariantResponseDto;
import com.web.SellShoes.entity.Color;
import com.web.SellShoes.entity.Product;
import com.web.SellShoes.entity.Size;
import com.web.SellShoes.entity.Variant;
import com.web.SellShoes.repository.VariantRepository;
import com.web.SellShoes.service.VariantService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VariantServiceImpl implements VariantService {
	private final VariantRepository variantRepository;

	@Override
	public List<Variant> getVariantsByProductId(Integer productId) {
		return variantRepository.findByProductId(productId);
	}

	@Override
	public void save(Variant variant) {
		variantRepository.save(variant);
	}

	@Override
	public Page<Variant> searchVariant(int pageNumber, int size, Size size2, Color color, String keyword) {
		PageRequest variantPageable = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.ASC, "price"));
		Page<Variant> variantPage = variantRepository.searchVariant(variantPageable, size2, color, keyword);

		return variantPage;
	}

	@Override
	public Optional<Variant> getVariantById(Integer variantId) {
		Optional<Variant> variant = variantRepository.getVariantById(variantId);
		return variant;
	}

	@Override
	public void deleteVariant(Variant variant) {
		variant.setDeleteAt(LocalDate.now());
		variantRepository.save(variant);
	}

	@Override
	public List<VariantResponseDto> getVariantByProduct(Product product) {
		List<Variant> variants = variantRepository.getVariantByProduct(product);
		List<VariantResponseDto> variantResponseDto = variants.stream()
				.map(variant -> new VariantResponseDto(variant.getId(), variant.getProduct().getId(),
						variant.getSize().getId(), variant.getColor().getId(), variant.getSize().getSize(),
						variant.getColor().getColor(), variant.getPrice(), variant.getCurrentPrice(),
						variant.getQuantity(), variant.getBuyCount()))
				.collect(Collectors.toList());
		return variantResponseDto;
	}

	@Override
	public Optional<Variant> getVariantBySizeAndColor(Integer productId ,Integer sizeId, Integer colorId) {
		Optional<Variant> variant = variantRepository.findVariantBySizeAndColor(productId,sizeId, colorId);
		return variant;
	}

	@Override
	public List<Variant> getVariantsByProductIdAndSizeIdColorId(Integer productId, Size size, Color color) {
		return variantRepository.findVariantBySizeIdAndColorId(productId, size, color);
	}


	public List<Variant> getListVariantsById(Integer variantId) {
		List<Variant> variants = variantRepository.getListVariantById(variantId);
		return variants;
	}
}
