package com.web.SellShoes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.SellShoes.entity.Color;
import com.web.SellShoes.entity.Size;
import com.web.SellShoes.entity.Variant;

@Repository
public interface VariantRepository extends JpaRepository<Variant, Integer> {
	List<Variant> findByProductId(Integer productId);
	
	@Query("SELECT p FROM Variant p WHERE p.deleteAt is null AND p.id = :variantId")
	public Optional<Variant> getVariantById(Integer variantId);

	@Query("SELECT p FROM Variant p WHERE p.deleteAt is null AND p.id = :variantId")
	public List<Variant> getListVariantById(Integer variantId);
	/*
	 * @Query("SELECT o FROM Variant o WHERE o.deleteAt is null AND (:size IS NULL OR o.size = :size) AND (:color IS NULL OR o.color = :color) AND (:keyword IS NULL OR o.price LIKE %:keyword% OR o.currentPrice LIKE %:keyword% OR o.quantity LIKE %:keyword% OR o.note LIKE %:keyword% )"
	 * ) Page<Variant> searchVariant(Pageable pageable, Size size, Color color,
	 * String keyword);
	 */
	
	@Query("SELECT o FROM Variant o JOIN o.product p WHERE o.deleteAt is null AND (:size IS NULL OR o.size = :size) AND (:color IS NULL OR o.color = :color) AND (:keyword IS NULL OR o.price LIKE %:keyword% OR o.currentPrice LIKE %:keyword% OR o.quantity LIKE %:keyword% OR o.note LIKE %:keyword% OR p.title LIKE %:keyword%)")
	Page<Variant> searchVariant(Pageable pageable, Size size, Color color, String keyword);

}
