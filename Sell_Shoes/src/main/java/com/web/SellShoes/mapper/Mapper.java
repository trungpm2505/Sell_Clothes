package com.web.SellShoes.mapper;


import java.util.Optional;

import org.springframework.stereotype.Component;

import com.web.SellShoes.dto.requestDto.ProductRequestDto;
import com.web.SellShoes.dto.requestDto.UserRequestDto;
import com.web.SellShoes.entity.Account;
import com.web.SellShoes.entity.Brand;
import com.web.SellShoes.entity.Category;
import com.web.SellShoes.entity.Product;
import com.web.SellShoes.service.BrandService;
import com.web.SellShoes.service.CategoryService;
import com.web.SellShoes.service.ImageService;
import com.web.SellShoes.service.ProductService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Mapper {
	private final CategoryService categoryService;
	private final ImageService imageService;
	private final ProductService productService;
	private final BrandService brandService;
	
	public Account userRquestDtoMapToUser(UserRequestDto userRequestDto) {
		Account user = new Account();
		user.setPassword(userRequestDto.getPassword());
		user.setFullName(userRequestDto.getFullname());
		user.setAddress(userRequestDto.getAddress());
		user.setPhone(userRequestDto.getPhone());
		user.setEmail(userRequestDto.getEmail());
		return user;
	}
	
	public Product productRequestDtoToProduct(ProductRequestDto productRequestDto) {
		Product product = new Product();
		if (productRequestDto.getId() != null) {
			product.setId(productRequestDto.getId());
		}
		// set title
		product.setTitle(productRequestDto.getTitle());

		// set category
		Optional<Category> category = categoryService.getCategory(productRequestDto.getCategory());
		product.setCategory(category.get());
		
		//set Brand
		Optional<Brand> brand = brandService.getBrand(productRequestDto.getBrand());
		product.setBrand(brand.get());

		// set discription
		product.setDiscription(productRequestDto.getDiscription());

		return product;

	}
}
