package com.web.SellShoes.serviceImpl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.web.SellShoes.entity.Product;
import com.web.SellShoes.repository.ProductRepository;
import com.web.SellShoes.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
	private final ProductRepository productRepository;

	@Override
	public Optional<Product> getProductById(Integer productId) {
		Optional<Product> product = productRepository.getProductById(productId);
		return product;
	}

	@Override
	public Optional<Product> getProductByTitle(String title) {
		Optional<Product> product = productRepository.getProductByTitle(title);
		return product;
	}

	@Override
	public void save(Product product) {
		productRepository.save(product);
	}

}
