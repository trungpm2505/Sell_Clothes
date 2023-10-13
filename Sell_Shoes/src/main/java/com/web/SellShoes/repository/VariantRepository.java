package com.web.SellShoes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.SellShoes.entity.Variant;

@Repository
public interface VariantRepository extends JpaRepository<Variant, Integer> {
	List<Variant> findByProductId(Integer productId);
}
