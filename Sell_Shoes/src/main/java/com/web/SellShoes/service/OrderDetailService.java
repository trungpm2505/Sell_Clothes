package com.web.SellShoes.service;

import java.util.List;

import com.web.SellShoes.entity.Order;
import com.web.SellShoes.entity.OrderDetail;


public interface OrderDetailService {
	public List<OrderDetail> getOrderDtails(Order order); 
	public int getNumberOfProductInOrder(Order order); 
}
