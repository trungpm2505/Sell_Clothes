package com.web.SellShoes.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.web.SellShoes.entity.Account;
import com.web.SellShoes.entity.Order;

public interface OrderService {
	public Page<Order> getOrderPage(int pageNumber, int size);

	public Page<Order> getOrderPageByStatus(int pageNumber, int size, int status);

	public Page<Order> findByDateRangeAndKey(int pageNumber, int size, LocalDate createAt, LocalDate startDate,
			LocalDate endDate, String key, int status);

	public Optional<Order> getOrderById(Integer orderId);

	public void save(Order order);

	public void updateOrderStatus(Order order, int status);
	
	public Page<Order> findOrderForReport(int pageNumber, int szie,LocalDate completedAt,LocalDate startDate,LocalDate endDate,String key);

	public Page<Order> getOrderPageByAccount(int pageNumber, int size, Account account);

	public Page<Order> getOrderPageByStatusAndAccount(int pageNumber, int size, int status, Account account);

	public Page<Order> getOrderPageByKeywordAndAccount(int pageNumber, int size, String keyWord, Account account);
}
