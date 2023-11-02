package com.web.SellShoes.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.SellShoes.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	@Query("SELECT p FROM Product p WHERE p.deleteAt is null AND p.title = :title")
	public Optional<Product> getProductByTitle(String title);

	@Query("SELECT p FROM Product p WHERE p.deleteAt is null AND p.id = :productId")
	public Optional<Product> getProductById(Integer productId);

	@Query("SELECT c FROM Product c WHERE c.deleteAt is null")
	Page<Product> findProductPage(Pageable pageable);

	@Query("SELECT c FROM Product c WHERE c.deleteAt is null AND c.title LIKE %:keyword%")
	Page<Product> findByKeyword(Pageable pageable, String keyword);

}
