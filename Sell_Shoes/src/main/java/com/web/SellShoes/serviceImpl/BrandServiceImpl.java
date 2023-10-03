package com.web.SellShoes.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.web.SellShoes.entity.Brand;
import com.web.SellShoes.repository.BrandRepository;
import com.web.SellShoes.service.BrandService;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService{
	private final BrandRepository brandRepository;

	@Override
	public Optional<Brand> getBrand(Integer brandId) {
		Optional<Brand> brand = brandRepository.getBrandById(brandId);
		return brand;
	}

	@Override
	public List<Brand> getAll() {
		List<Brand> brands = brandRepository.findAll();
		return brands;
	}
	
}
