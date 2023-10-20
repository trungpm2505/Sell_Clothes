package com.web.SellShoes.mapper;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.web.SellShoes.dto.requestDto.ProductRequestDto;
import com.web.SellShoes.dto.requestDto.UserRequestDto;
import com.web.SellShoes.dto.requestDto.VariantRequestDto;
import com.web.SellShoes.entity.Account;
import com.web.SellShoes.entity.Brand;
import com.web.SellShoes.entity.Category;
import com.web.SellShoes.entity.Color;
import com.web.SellShoes.entity.Product;
import com.web.SellShoes.entity.Size;
import com.web.SellShoes.entity.Variant;
import com.web.SellShoes.service.BrandService;
import com.web.SellShoes.service.CategoryService;
import com.web.SellShoes.service.ColorService;
import com.web.SellShoes.service.ProductService;
import com.web.SellShoes.service.SizeService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Mapper {
	private final CategoryService categoryService;
	private final ProductService productService;
	private final BrandService brandService;
	private final ColorService colorService;
	private final SizeService sizeService;

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

		// set Brand
		Optional<Brand> brand = brandService.getBrand(productRequestDto.getBrand());
		product.setBrand(brand.get());

		// set discription
		product.setDiscription(productRequestDto.getDiscription());

		return product;

	}

	public Variant variantRequestDtoToVariant(VariantRequestDto variantRequestDto) {
		Variant variant = new Variant();

		if (variantRequestDto.getVariantId() != null) {
			variant.setId(variantRequestDto.getVariantId());
		}

		Optional<Product> product = productService.getProductById(variantRequestDto.getProduct());
		variant.setProduct(product.get());

		Optional<Color> color = colorService.getColor(variantRequestDto.getColor());
		variant.setColor(color.get());

		Optional<Size> size = sizeService.getSize(variantRequestDto.getSize());
		variant.setSize(size.get());

		variant.setPrice(variantRequestDto.getPrice());
		if (variantRequestDto.getCurrentPrice() != 0) {
			variant.setCurrentPrice(variantRequestDto.getCurrentPrice());
		}

		variant.setQuantity(variantRequestDto.getQuantity());

		if (variantRequestDto.getNote() != null) {
			variant.setNote(variantRequestDto.getNote());
		}

		return variant;

	}
}
