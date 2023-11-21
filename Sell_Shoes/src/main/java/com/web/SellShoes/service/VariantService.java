package com.web.SellShoes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.web.SellShoes.entity.Color;
import com.web.SellShoes.entity.Size;
import com.web.SellShoes.entity.Variant;

public interface VariantService {
	public List<Variant> getVariantsByProductId(Integer productId);

	public void save(Variant variant);

	public Page<Variant> searchVariant(int pageNumber, int size, Size size2, Color color, String keyword);
	
	public Optional<Variant> getVariantById(Integer variantId);
	
	public List<Variant> getListVariantsById(Integer variantId);
	
	public void deleteVariant(Variant variant);
}
