package com.web.SellShoes.serviceImpl;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.web.SellShoes.entity.Order;
import com.web.SellShoes.repository.OrderRepository;
import com.web.SellShoes.service.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderService {
	private final OrderRepository orderRepository;

	@Override
	public Page<Order> getOrderPage(int pageNumber, int size) {
		PageRequest orderPageable = PageRequest.of(pageNumber, size, Sort.by(Direction.DESC, "id"));

		Page<Order> orderPage = orderRepository.findAll(orderPageable);

		return orderPage;

	}

	@Override
	public Page<Order> findByDateRangeAndKey(int pageNumber, int size, LocalDate createAt, LocalDate startDate,
			LocalDate endDate, String key, int status) {
		PageRequest orderPageable = PageRequest.of(pageNumber, size, Sort.by(Direction.DESC, "id"));
		return orderRepository.findByDateRangeAndKeyword(orderPageable, createAt, startDate, endDate, key, status);
	}

	@Override
	public Page<Order> getOrderPageByStatus(int pageNumber, int size, int status) {
		PageRequest orderPageable = PageRequest.of(pageNumber, size, Sort.by(Direction.DESC, "id"));
		Page<Order> orderPage = orderRepository.findOrderByStatus(orderPageable, status);
		return orderPage;
	}

}
