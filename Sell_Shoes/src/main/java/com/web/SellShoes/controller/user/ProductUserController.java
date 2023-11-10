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
import com.web.SellShoes.entity.Product;
import com.web.SellShoes.mapper.Mapper;
import com.web.SellShoes.service.ImageService;
import com.web.SellShoes.service.ProductService;
import com.web.SellShoes.service.VariantService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "product")
public class ProductUserController {
	private final ProductService productService;
	private final ImageService imageService;
	private final VariantService variantService;
	
	
	@GetMapping("/getProductView")
	public ResponseEntity<ProductPageResponseDto> getProductView(@RequestParam(defaultValue = "7") int size,
			@RequestParam(defaultValue = "0") int page, @RequestParam(required = false) String keyword) {
		keyword = "";
		Page<Product> productPage = null;
		if (keyword.equals("")) {
			productPage = productService.getAllProduct(page, size);
		} else {
			productPage = productService.getProductByKey(page, size, keyword);
		}
		//List<Product> products = productService.getAll();

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
	
	@GetMapping(value="/all-product")
	public String getAllProduct(HttpSession session,Model model) {
		return "user/product/demo";
	}
}
