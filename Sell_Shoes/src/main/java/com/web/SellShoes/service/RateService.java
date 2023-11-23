package com.web.SellShoes.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.web.SellShoes.entity.Order;
import com.web.SellShoes.entity.Rate;
import com.web.SellShoes.entity.Variant;

public interface RateService {
	public List<Rate> getRateByOrder(Order order);

	public Page<Rate> finPage(int pageNumber, int size, Variant variant);

	public Page<Rate> finPageByRateScore(int pageNumber, int size, Variant variant, int rateScore);

	public Rate finPageByOrderAndVariant(Order order, Variant variant);

	public void save(Rate rate);

	public Rate getRateById(Integer rateId);
}
