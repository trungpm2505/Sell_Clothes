package com.web.SellShoes.service;

import java.util.List;

import com.web.SellShoes.entity.Order;
import com.web.SellShoes.entity.Rate;


public interface RateService {
	public List<Rate> getRateByOrder(Order order);
}
