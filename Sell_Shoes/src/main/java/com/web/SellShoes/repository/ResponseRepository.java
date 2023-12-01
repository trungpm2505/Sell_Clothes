package com.web.SellShoes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.SellShoes.entity.Rate;
import com.web.SellShoes.entity.ResponseRate;

@Repository
public interface ResponseRepository extends JpaRepository<ResponseRate, Integer>{
	public List<ResponseRate> getResponsesByRate(Rate rate);
}
