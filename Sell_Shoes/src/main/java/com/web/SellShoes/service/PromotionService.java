package com.web.SellShoes.service;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.web.SellShoes.dto.responseDto.PromotionResponseDto;
import com.web.SellShoes.entity.Promotion;


public interface PromotionService {
	public Optional<Promotion> getPromotionById(Integer promotionId);
	public Optional<Promotion> getPromotionByCouponCode(String couponCode);
	public void save(Promotion promotion);
	public Page<Promotion> getAllPromotion(int pageNumber, int size);
	public Page<Promotion> getPromotionByKey(int pageNumber, int size,String keyWord);
	public void deletePromotion(Promotion promotion);
	

}
