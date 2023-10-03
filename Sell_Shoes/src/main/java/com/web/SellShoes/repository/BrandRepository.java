package com.web.SellShoes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.SellShoes.entity.Brand;
@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer>{
	@Query("SELECT c FROM Brand c WHERE c.deleteAt is null AND c.id = :brandId")
	public Optional<Brand> getBrandById(Integer brandId);
}
