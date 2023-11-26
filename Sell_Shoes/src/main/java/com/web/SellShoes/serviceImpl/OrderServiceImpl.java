package com.web.SellShoes.serviceImpl;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.web.SellShoes.entity.Account;
import com.web.SellShoes.entity.Order;
import com.web.SellShoes.repository.OrderRepository;
import com.web.SellShoes.service.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
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

	@Override
	public Optional<Order> getOrderById(Integer orderId) {
		Optional<Order> order = orderRepository.getOrderById(orderId);
		return order;
	}

	@Override
	public void save(Order order) {
		orderRepository.save(order);
	}

	@Override
	public void updateOrderStatus(Order order, int status) {
		order.setStatus(status);
		orderRepository.save(order);
	}

	@Override
	public Page<Order> findOrderForReport(int pageNumber, int size, LocalDate completedAt, LocalDate startDate,
			LocalDate endDate, String key) {
		PageRequest orderPageable = PageRequest.of(pageNumber, size,
				Sort.by(Direction.DESC, "completedAt").and(Sort.by(Direction.DESC, "id")));
		return orderRepository.findOrderForReport(orderPageable, completedAt, startDate, endDate, key);
	}

	@Override
	public Page<Order> getOrderPageByAccount(int pageNumber, int size, Account account) {
		PageRequest orderPageable = PageRequest.of(pageNumber, size,
				Sort.by(Direction.DESC, "fullName").and(Sort.by(Direction.DESC, "id")));
		return orderRepository.findByAccount(orderPageable, account);
	}

	@Override
	public Page<Order> getOrderPageByStatusAndAccount(int pageNumber, int size, int status, Account account) {
		PageRequest orderPageable = PageRequest.of(pageNumber, size,
				Sort.by(Direction.DESC, "fullName").and(Sort.by(Direction.DESC, "id")));
		return orderRepository.findOrderByStatusAndAccount(orderPageable, status, account);
	}

	@Override
	public Page<Order> getOrderPageByKeywordAndAccount(int pageNumber, int size, String keyWord, Account account) {
		PageRequest orderPageable = PageRequest.of(pageNumber, size,
				Sort.by(Direction.DESC, "fullName").and(Sort.by(Direction.DESC, "id")));
		return orderRepository.findByKeywordForAccount(orderPageable, account, keyWord);
	}

}
