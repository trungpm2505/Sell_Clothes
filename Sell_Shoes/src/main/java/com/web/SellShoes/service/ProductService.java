package com.web.SellShoes.service;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.web.SellShoes.entity.Product;

public interface ProductService {
	public Optional<Product> getProductById(Integer productId);
	public Optional<Product> getProductByTitle(String title);
	public void save(Product product);
	public Page<Product> getAllProduct(int pageNumber, int size);
	public Page<Product> getProductByKey(int pageNumber, int size,String keyWord);
	public void deleteProduct(Product product);
}
