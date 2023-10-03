package com.web.SellShoes.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.web.SellShoes.dto.requestDto.ProductRequestDto;
import com.web.SellShoes.entity.Product;
import com.web.SellShoes.mapper.Mapper;
import com.web.SellShoes.service.ImageService;
import com.web.SellShoes.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/product")
public class ProductController {
	private final ProductService productService;
	private final Mapper mapper;
	private final ImageService imageService;
	
	@GetMapping(value = "/admin")
	public String getProductView(HttpSession session, Model model) {
		
		return "admin/product/add";
	}
	
	@PostMapping(value = "/add", consumes = "multipart/form-data", produces = { "application/json", "application/xml" })
	@Transactional
	@ResponseBody
	public ResponseEntity<?> saveProduct(@RequestParam(value = "file", required = false) MultipartFile[] files,
			 @RequestParam(required = false, defaultValue = "0") int defaultFileIndex, @Valid @ModelAttribute ProductRequestDto productRequestDto,
			BindingResult bindingResult){
		
		Map<String, Object> errors = new HashMap<>();
		if (bindingResult.hasErrors()) {
			errors.put("bindingErrors", bindingResult.getAllErrors());
		}
		if (files == null || files.length == 0) {
			errors.put("fileErrors","Please select at least 1 photo!");
		}
		Optional<Product> productByTitle = productService.getProductByTitle(productRequestDto.getTitle());
		if (productByTitle.isPresent()) {
			errors.put("titleDuplicate", "Title already exists! please enter a new title. ");
		}
		if (!errors.isEmpty()) {
			return ResponseEntity.badRequest().body(errors);
		}
		
		Product product = mapper.productRequestDtoToProduct(productRequestDto);
		productService.save(product);
		
		imageService.uploadFile(files, defaultFileIndex, product);
		
		return ResponseEntity.ok().body("{\"message\": \"Add product successfully\"}");
	}
}
