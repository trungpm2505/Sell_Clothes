package com.web.SellShoes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.web.SellShoes.entity.Brand;
import com.web.SellShoes.entity.Category;
import com.web.SellShoes.entity.Color;
import com.web.SellShoes.entity.Product;
import com.web.SellShoes.entity.Size;

public interface ProductService {
	public Optional<Product> getProductById(Integer productId);

	public Optional<Product> getProductByTitle(String title);

	public void save(Product product);

	public Page<Product> getAllProduct(int pageNumber, int size);

	public Page<Product> getProductByKey(int pageNumber, int size, String keyWord);

	public void deleteProduct(Product product);

	public List<Product> getAll();

	public Page<Product> searchProduct(int pageNumber, int size, Category category, Brand brand, Size size2,
			Color color, Float minPrice, Float maxPrice, String keyword);
}
