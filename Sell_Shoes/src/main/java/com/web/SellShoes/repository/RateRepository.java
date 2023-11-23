package com.web.SellShoes.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.SellShoes.entity.Order;
import com.web.SellShoes.entity.Rate;
import com.web.SellShoes.entity.Variant;

@Repository
public interface RateRepository extends JpaRepository<Rate, Integer>{
	public List<Rate> findRateByOrder(Order order);
	Page<Rate> findRateByVariant(Pageable pageable,Variant variant);
	Page<Rate> findRateByVariantAndRating(Pageable pageable,Variant variant,int rating);
	Rate findRateById(Integer id);
	public Rate findRateByOrderAndVariant(Order order,Variant variant);
}
