package com.web.SellShoes.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import com.web.SellShoes.entity.Brand;

public interface BrandService {

	Optional<Brand> findByBrandName(String brandName);
	
	Optional<Brand> findByDescriptionName(String descriptionName);
	
	Optional<Brand> findByThumbnailName(String thumbnailName);


	Page<Brand> getBrandByKey(int pagenumber, int size, String keyWord);

	Page<Brand> getAllBrand(int pagenumber, int size);

	Page<Brand> findBrandPage(Pageable pageable);

	void delete(Brand entity);

	Optional<Brand> findById(Integer id);

	Optional<Brand> getBrandById(Integer brandId);

	List<Brand> findAll();

	<S extends Brand> S save(S entity);

	List<Brand> getAll();

	Optional<Brand> getBrand(Integer brandId);

}
