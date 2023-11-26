package com.web.SellShoes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.SellShoes.entity.Order;
import com.web.SellShoes.entity.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer>{
	public List<OrderDetail> findOrderDetailsByOrder(Order order);
	
	@Query("SELECT count(*) FROM OrderDetail o WHERE o.order = :order")
	public int getNumberOfProductInOrder(Order order); 
}
