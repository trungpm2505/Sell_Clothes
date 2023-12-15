package com.web.SellShoes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.SellShoes.entity.Category;
import com.web.SellShoes.entity.Order;
import com.web.SellShoes.entity.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer>{
	public List<OrderDetail> findOrderDetailsByOrder(Order order);
	
	@Query("SELECT count(*) FROM OrderDetail o WHERE o.order = :order")
	public int getNumberOfProductInOrder(Order order); 
	
	@Query("SELECT o FROM OrderDetail o WHERE o.order.status = 4")
	public List<OrderDetail> findAll();
	
	@Query("SELECT count(o.quantity) FROM OrderDetail o WHERE o.variant.product.category = :category AND o.order.status = 4")
	public int getQuantityOfProductAndCategory(Category category);
}
