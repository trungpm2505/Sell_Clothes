package com.web.SellShoes.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.web.SellShoes.entity.Brand;
import com.web.SellShoes.entity.Category;
import com.web.SellShoes.entity.Color;
import com.web.SellShoes.entity.Product;
import com.web.SellShoes.entity.Size;
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

	@Override
	public Page<Product> getAllProduct(int pageNumber, int size) {
		// phân trang dữ liệu: số trang, kich thước, sắp xếp
		PageRequest productPageable = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.ASC, "title"));

		// lấy danh sách
		Page<Product> productPage = productRepository.findProductPage(productPageable);

		return productPage;
	}

	@Override
	public Page<Product> getProductByKey(int pageNumber, int size, String keyWord) {
		PageRequest productPageable = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.ASC, "title"));

		return productRepository.findByKeyword(productPageable, keyWord);
	}

	@Override
	public void deleteProduct(Product product) {
		product.setDeleteAt(LocalDate.now());
		productRepository.save(product);
	}

	@Override
	public List<Product> getAll() {
		return productRepository.findAll();
	}

	@Override
	public Page<Product> searchProduct(int pageNumber, int size, Category category, Brand brand, Size size2,
			Color color, Float minPrice, Float maxPrice, String keyword) {
		PageRequest productPageable = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.ASC, "createAt"));
		Page<Product> productPage = productRepository.searchProduct(productPageable, category, brand, size2, color,
				minPrice, maxPrice, keyword);
		return productPage;
	}

}
