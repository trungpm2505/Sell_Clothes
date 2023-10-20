package com.web.SellShoes.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.web.SellShoes.entity.Variant;
import com.web.SellShoes.repository.VariantRepository;
import com.web.SellShoes.service.VariantService;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class VariantServiceImpl implements VariantService{
	private final VariantRepository variantRepository;

	@Override
	public List<Variant> getVariantsByProductId(Integer productId) {
		return variantRepository.findByProductId(productId);
	}

}
