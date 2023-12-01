package com.web.SellShoes.serviceImpl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.web.SellShoes.entity.Order;
import com.web.SellShoes.entity.Product;
import com.web.SellShoes.entity.Rate;
import com.web.SellShoes.entity.Variant;
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

	@Override
	public Page<Rate> finPage(int pageNumber, int size, Variant variant) {
		PageRequest ratePageable = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.DESC, "createAt"));
		return rateRepository.findRateByVariant(ratePageable, variant);
	}

	@Override
	public Page<Rate> finPageByRateScore(int pageNumber, int size, Variant variant, int rateScore) {
		PageRequest ratePageable = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.DESC, "createAt"));
		return rateRepository.findRateByVariantAndRating(ratePageable, variant, rateScore);
	}

	@Override
	public Rate finPageByOrderAndVariant(Order order, Variant variant) {
		return rateRepository.findRateByOrderAndVariant(order, variant);
	}

	@Override
	public void save(Rate rate) {
		rateRepository.save(rate);
	}

	@Override
	public Rate getRateById(Integer rateId) {
		return rateRepository.findRateById(rateId);
	}

	@Override
	public Page<Rate> finPageProduct(int pageNumber, int size, Product product) {
		PageRequest ratePageable = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.DESC, "createAt"));
		return rateRepository.findRateByProduct(ratePageable, product);
	}

	@Override
	public Page<Rate> finPageByRateScoreProduct(int pageNumber, int size, Product product, int rateScore) {
		PageRequest ratePageable = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.DESC, "createAt"));
		return rateRepository.findRateByProductAndRating(ratePageable, product, rateScore);
	}

}
