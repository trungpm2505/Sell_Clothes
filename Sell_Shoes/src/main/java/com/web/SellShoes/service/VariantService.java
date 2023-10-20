package com.web.SellShoes.service;

import java.util.List;

import com.web.SellShoes.entity.Variant;

public interface VariantService {
	public List<Variant> getVariantsByProductId(Integer productId);
}
