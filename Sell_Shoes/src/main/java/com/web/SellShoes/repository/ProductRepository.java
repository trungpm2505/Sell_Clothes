package com.web.SellShoes.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.web.SellShoes.entity.Brand;
import com.web.SellShoes.entity.Category;
import com.web.SellShoes.entity.Color;
import com.web.SellShoes.entity.Product;
import com.web.SellShoes.entity.Size;

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

	@Query("SELECT DISTINCT p FROM Product p " +
	        "JOIN p.variants v " +
	        "WHERE p.deleteAt IS NULL AND v.deleteAt IS NULL " +
	        "AND (:category IS NULL OR p.category = :category) " +
	        "AND (:brand IS NULL OR p.brand = :brand) " +
	        "AND (:size IS NULL OR v.size = :size) " +
	        "AND (:color IS NULL OR v.color = :color) " +
	        "AND ((:minPrice IS NULL AND :maxPrice IS NULL) OR (v.price BETWEEN :minPrice AND :maxPrice OR v.currentPrice BETWEEN :minPrice AND :maxPrice)) " +
	        "AND (:keyword IS NULL OR p.title LIKE %:keyword%)")
	Page<Product> searchProduct(Pageable pageable, Category category, Brand brand, Size size, Color color,
	                            Float minPrice, Float maxPrice, String keyword);


	@Query("SELECT c FROM Product c WHERE c.deleteAt is null")
	List<Product> findAll();

}
