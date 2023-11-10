package com.web.SellShoes.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.SellShoes.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
	@Query("SELECT o FROM Order o WHERE (:createAt IS NULL OR o.order_date = :createAt) AND (:startDate IS NULL OR o.order_date >= :startDate) AND (:endDate IS NULL OR o.order_date <= :endDate) AND (o.fullName LIKE %:keyword% OR o.phone_Number LIKE %:keyword% OR o.adrress LIKE %:keyword% OR o.note LIKE %:keyword% OR o.id LIKE %:keyword% OR o.totalMoney LIKE %:keyword% OR o.note LIKE %:keyword%) AND (:status = 0 OR o.status = :status)")
	Page<Order> findByDateRangeAndKeyword(Pageable pageable, LocalDate createAt, LocalDate startDate, LocalDate endDate,
			String keyword, int status);

	Page<Order> findOrderByStatus(Pageable pageable, int status);
}
