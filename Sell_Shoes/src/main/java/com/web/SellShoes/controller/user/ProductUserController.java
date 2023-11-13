package com.web.SellShoes.controller.user;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.SellShoes.dto.responseDto.ProductPageResponseDto;
import com.web.SellShoes.dto.responseDto.ProductResponseDto;
import com.web.SellShoes.entity.Brand;
import com.web.SellShoes.entity.Category;
import com.web.SellShoes.entity.Color;
import com.web.SellShoes.entity.Product;
import com.web.SellShoes.entity.Size;
import com.web.SellShoes.mapper.Mapper;
import com.web.SellShoes.service.BrandService;
import com.web.SellShoes.service.CategoryService;
import com.web.SellShoes.service.ColorService;
import com.web.SellShoes.service.ImageService;
import com.web.SellShoes.service.ProductService;
import com.web.SellShoes.service.SizeService;
import com.web.SellShoes.service.VariantService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "product")
public class ProductUserController {
	private final ProductService productService;
	private final CategoryService categoryService;
	private final BrandService brandService;
	private final ColorService colorService;
	private final SizeService sizeService;
	private final ImageService imageService;
	private final VariantService variantService;

	@GetMapping("/getProductView")
	public ResponseEntity<ProductPageResponseDto> getProductView(@RequestParam(defaultValue = "9") int size,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(required = false, defaultValue = "0") Integer categoryId,
			@RequestParam(required = false, defaultValue = "0") Integer brandId,
			@RequestParam(required = false, defaultValue = "0") Integer sizeId,
			@RequestParam(required = false, defaultValue = "0") Integer colorId,
			@RequestParam(required = false) String keyword) {
		keyword = "";
		Page<Product> productPage = null;
		Category category = null;
		Brand brand = null;
		Size size2 = null;
		Color color = null;
		
		if (categoryId != 0) {
			category = categoryService.getCategoryById(categoryId).get();
		}
		if (brandId != 0) {
			brand = brandService.getBrand(brandId).get();
		}
		if (sizeId != 0) {
			size2 = sizeService.getSize(sizeId).get();
		}
		if (colorId != 0) {
			color = colorService.getColor(colorId).get();
		}
		
		productPage = productService.searchProduct(page, size, category, brand, size2, color, keyword);

		List<ProductResponseDto> productResponseDtos = new ArrayList<>();

		for (Product product : productPage) {
			ProductResponseDto productResponseDto = new ProductResponseDto(product.getId(), product.getTitle(),
					product.getDiscription(), imageService.getImageByProduct(product),
					variantService.getVariantByProduct(product));

			productResponseDtos.add(productResponseDto);
		}
		ProductPageResponseDto productPageResponseDto = new ProductPageResponseDto(productPage.getTotalPages(),
				productPage.getNumber(), productPage.getSize(), productResponseDtos);

		return ResponseEntity.ok(productPageResponseDto);
	}

	@GetMapping(value = "/all-product")
	public String getAllProduct(HttpSession session, Model model) {
		return "shop/shopcontent/shop1";
	}
	@GetMapping("/all-product1")
	public String checkout() {
		// test từng trang thay đường dẫn
		return "user/product/demo";
	}
}
