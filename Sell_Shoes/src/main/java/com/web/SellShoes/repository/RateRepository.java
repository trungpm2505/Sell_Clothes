package com.web.SellShoes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.SellShoes.entity.Order;
import com.web.SellShoes.entity.Rate;

@Repository
public interface RateRepository extends JpaRepository<Rate, Integer>{
	public List<Rate> findRateByOrder(Order order);
}
