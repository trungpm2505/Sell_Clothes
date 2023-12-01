package com.web.SellShoes.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.SellShoes.entity.Account;
import com.web.SellShoes.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
	@Query("SELECT o FROM Order o WHERE (:createAt IS NULL OR o.order_date = :createAt) AND (:startDate IS NULL OR o.order_date >= :startDate) AND (:endDate IS NULL OR o.order_date <= :endDate) AND (o.fullName LIKE %:keyword% OR o.phone_Number LIKE %:keyword% OR o.adrress LIKE %:keyword% OR o.note LIKE %:keyword% OR o.id LIKE %:keyword% OR o.totalMoney LIKE %:keyword% OR o.note LIKE %:keyword%) AND (:status = 0 OR o.status = :status)")
	Page<Order> findByDateRangeAndKeyword(Pageable pageable, LocalDate createAt, LocalDate startDate, LocalDate endDate,
			String keyword, int status);

	Page<Order> findOrderByStatus(Pageable pageable, int status);

	public Optional<Order> getOrderById(Integer orderId);

	Page<Order> findByAccount(Pageable pageable, Account account);

	Page<Order> findOrderByStatusAndAccount(Pageable pageable, int status, Account account);

	@Query("SELECT o FROM Order o WHERE (o.account = :account) AND (:keyword IS NULL OR o.account.fullName LIKE %:keyword% OR o.id LIKE %:keyword% )")
	Page<Order> findByKeywordForAccount(Pageable pageable, Account account, String keyword);

	@Query("SELECT o FROM Order o WHERE (:completedAt IS NULL OR o.completedAt = :completedAt) AND (:startDate IS NULL OR o.completedAt >= :startDate) AND (:endDate IS NULL OR o.completedAt <= :endDate) AND (:keyword IS NULL OR o.account.fullName LIKE %:keyword% OR o.fullName LIKE %:keyword% OR o.phone_Number LIKE %:keyword% OR o.adrress LIKE %:keyword% OR o.note LIKE %:keyword% OR o.id LIKE %:keyword% OR o.note LIKE %:keyword%) AND (o.status = 4)")
	Page<Order> findOrderForReport(Pageable pageable, LocalDate completedAt, LocalDate startDate, LocalDate endDate,
			String keyword);
}
