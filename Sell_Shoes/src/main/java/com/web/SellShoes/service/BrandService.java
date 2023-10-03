package com.web.SellShoes.service;

import java.util.List;
import java.util.Optional;

import com.web.SellShoes.entity.Brand;
public interface BrandService {
	public Optional<Brand> getBrand(Integer brandId);
	public List<Brand> getAll();
}
