package com.web.SellShoes.service;

import java.util.Optional;

import com.web.SellShoes.entity.Product;

public interface ProductService {
	public Optional<Product> getProductById(Integer productId);
	public Optional<Product> getProductByTitle(String title);
	public void save(Product product);
}
