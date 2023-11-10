package com.web.SellShoes.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.web.SellShoes.entity.Order;
import com.web.SellShoes.entity.Rate;
import com.web.SellShoes.repository.RateRepository;
import com.web.SellShoes.service.RateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RateServiceImpl implements RateService {
	private final RateRepository rateRepository;
	
	@Override
	public List<Rate> getRateByOrder(Order order) {
		return rateRepository.findRateByOrder(order);
	}

}
