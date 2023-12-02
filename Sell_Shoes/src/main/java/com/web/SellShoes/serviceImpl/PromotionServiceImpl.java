package com.web.SellShoes.serviceImpl;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.web.SellShoes.dto.responseDto.PromotionResponseDto;
import com.web.SellShoes.entity.Promotion;
import com.web.SellShoes.repository.PromotionRepository;
import com.web.SellShoes.service.PromotionService;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor

public class PromotionServiceImpl implements PromotionService{
	private final PromotionRepository promotionRepository;
	@Override
	public Optional<Promotion> getPromotionById(Integer promotionId) {
		Optional<Promotion> promotion = promotionRepository.getPromotionById(promotionId);
		return promotion;
	}

	@Override
	public Optional<Promotion> getPromotionByCouponCode(String couponCode) {
		Optional<Promotion> promotion = promotionRepository.getPromotionByCouponCode(couponCode);
		return promotion;
	}

	@Override
	public void save(Promotion Promotion) {
		promotionRepository.save(Promotion);
	}
	
	@Override
	public Page<Promotion> getAllPromotion(int pageNumber, int size) {
		// phân trang dữ liệu: số trang, kich thước, sắp xếp
		PageRequest PromotionPageable = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.ASC, "id"));

		// lấy danh sách
		Page<Promotion> PromotionPage = promotionRepository.findPromotionPage(PromotionPageable);

		return PromotionPage;
	}

	@Override
	public Page<Promotion> getPromotionByKey(int pageNumber, int size, String keyWord) {
		PageRequest promotionPageable = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.ASC, "id"));

		return promotionRepository.findByKeyword(promotionPageable, keyWord);
	}
	
	@Override
	public void deletePromotion(Promotion promotion) {
		promotion.setDeleteAt(LocalDate.now());
		promotionRepository.save(promotion);
	}

	
}
