package com.web.SellShoes.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.web.SellShoes.entity.Order;
import com.web.SellShoes.entity.OrderDetail;
import com.web.SellShoes.repository.OrderDetailRepository;
import com.web.SellShoes.service.OrderDetailService;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService{
	private final OrderDetailRepository orderDetailRepository;

	@Override
	public List<OrderDetail> getOrderDtails(Order order) {
		return orderDetailRepository.findOrderDetailsByOrder(order);
	}

	@Override
	public <S extends OrderDetail> S save(S entity) {
		return orderDetailRepository.save(entity);
		
	}
	public int getNumberOfProductInOrder(Order order) {
		return orderDetailRepository.getNumberOfProductInOrder(order);
	}

}
