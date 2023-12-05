package com.web.SellShoes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.SellShoes.entity.Brand;
import com.web.SellShoes.entity.Brand;
@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer>{
	@Query("SELECT c FROM Brand c WHERE c.deleteAt is null AND c.id = :brandId")
	public Optional<Brand> getBrandById(Integer brandId);
	@Query("SELECT c FROM Brand c WHERE c.deleteAt is null")
	Page<Brand> findBrandPage(Pageable pageable);

	@Query("SELECT c FROM Brand c WHERE c.deleteAt is null AND c.name LIKE %:keyword%")
	Page<Brand> findByKeyWord(Pageable pageable, String keyword);

	@Query("SELECT c FROM Brand c WHERE c.deleteAt is null AND c.name = :brandName")
	Optional<Brand> findByBrandName(String brandName);
	
	@Query("SELECT c FROM Brand c WHERE c.deleteAt is null AND c.name = :descriptionName")
	Optional<Brand> findByDescriptionName(String descriptionName);
	
	@Query("SELECT c FROM Brand c WHERE c.deleteAt is null AND c.name = :thumbnailName")
	Optional<Brand> findByThumbnailName(String thumbnailName);
	
	@Query("SELECT c FROM Brand c WHERE c.deleteAt is null")
	List<Brand> findAll();

}

