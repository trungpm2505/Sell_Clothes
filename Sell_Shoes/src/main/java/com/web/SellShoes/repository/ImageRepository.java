package com.web.SellShoes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.SellShoes.entity.Image;
import com.web.SellShoes.entity.Product;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer>{
	@Query("SELECT i FROM Image i WHERE i.product = :product")
	public List<Image> getImageByProduct(Product product);
}
