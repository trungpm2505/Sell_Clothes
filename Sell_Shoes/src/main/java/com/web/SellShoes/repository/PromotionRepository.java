package com.web.SellShoes.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.SellShoes.entity.Promotion;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer>{
	@Query("SELECT p FROM Promotion p WHERE p.deleteAt is null AND p.couponCode = :couponCode")
	public Optional<Promotion> getPromotionByCouponCode(String couponCode);

	@Query("SELECT p FROM Promotion p WHERE p.deleteAt is null AND p.id = :promotionId")
	public Optional<Promotion> getPromotionById(Integer promotionId);

	@Query("SELECT c FROM Promotion c WHERE c.deleteAt is null")
	Page<Promotion> findPromotionPage(Pageable pageable);

	@Query("SELECT c FROM Promotion c WHERE c.deleteAt is null AND c.name LIKE %:keyword% or c.couponCode LIKE %:keyword% or c.maximumDiscountValue LIKE %:keyword%")
	Page<Promotion> findByKeyword(Pageable pageable, String keyword);
}
