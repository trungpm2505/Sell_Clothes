package com.web.SellShoes.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.web.SellShoes.entity.Brand;
import com.web.SellShoes.repository.BrandRepository;
import com.web.SellShoes.service.BrandService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
	public final BrandRepository brandRepository;

	@Override
	public Optional<Brand> getBrand(Integer brandId) {
		Optional<Brand> brand = brandRepository.getBrandById(brandId);
		return brand;
	}

	@Override
	public List<Brand> getAll() {
		List<Brand> brand = brandRepository.findAll();
		return brand;
	}

	@Override
	public <S extends Brand> S save(S entity) {
		return brandRepository.save(entity);
	}

	@Override
	public List<Brand> findAll() {
		return brandRepository.findAll();
	}

	@Override
	public Optional<Brand> getBrandById(Integer brandId) {
		return brandRepository.getBrandById(brandId);
	}

	@Override
	public Optional<Brand> findById(Integer id) {
		return brandRepository.findById(id);
	}

	@Override
	public void delete(Brand entity) {
		entity.setDeleteAt(LocalDate.now());
		brandRepository.save(entity);
	}

	@Override
	public Page<Brand> findBrandPage(Pageable pageable) {
		return brandRepository.findBrandPage(pageable);
	}

	@Override
	public Page<Brand> getAllBrand(int pagenumber, int size) {
		PageRequest brandPageable = PageRequest.of(pagenumber, size, Sort.by(Sort.Direction.ASC, "name"));

		Page<Brand> brandPage = brandRepository.findBrandPage(brandPageable);
	
	    return brandPage;
	}

	@Override
	public Page<Brand> getBrandByKey(int pagenumber, int size, String keyWord) {
		PageRequest brandPageable = PageRequest.of(pagenumber, size, Sort.by(Sort.Direction.ASC, "name"));

		return brandRepository.findByKeyWord(brandPageable, keyWord);
	}

	@Override
	public Optional<Brand> findByBrandName(String brandName) {
		return brandRepository.findByBrandName(brandName);
	}

	

	@Override
	public Optional<Brand> findByDescriptionName(String descriptionName) {
		// TODO Auto-generated method stub
		return brandRepository.findByDescriptionName(descriptionName);
	}

	@Override
	public Optional<Brand> findByThumbnailName(String thumbnailName) {
		// TODO Auto-generated method stub
		return brandRepository.findByThumbnailName(thumbnailName);
	}
	

}
