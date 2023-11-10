package com.web.SellShoes.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;

import com.web.SellShoes.entity.Order;

public interface OrderService {
	public Page<Order> getOrderPage(int pageNumber, int size);
	public Page<Order> getOrderPageByStatus(int pageNumber, int size,int status);

	public Page<Order> findByDateRangeAndKey(int pageNumber, int size, LocalDate createAt, LocalDate startDate,
			LocalDate endDate, String key, int status);
}
