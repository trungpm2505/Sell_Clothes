package com.web.SellShoes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.SellShoes.entity.Image;
import com.web.SellShoes.entity.Product;
import com.web.SellShoes.entity.Rate;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer>{
	@Query("SELECT i FROM Image i WHERE i.product = :product")
	public List<Image> getImageByProduct(Product product);
	
	@Query("SELECT i FROM Image i WHERE i.product = :product and i.isDefault = true")
	public Optional<Image> getImageByProductAndDefault(Product product);
	
	
	public List<Image> getImageByRate(Rate rate);
}
