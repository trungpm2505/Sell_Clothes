package com.web.SellShoes.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.web.SellShoes.dto.requestDto.ProductRequestDto;
import com.web.SellShoes.dto.responseDto.ProductPageResponseDto;
import com.web.SellShoes.dto.responseDto.ProductResponseDto;
import com.web.SellShoes.entity.Product;
import com.web.SellShoes.entity.Variant;
import com.web.SellShoes.mapper.Mapper;
import com.web.SellShoes.service.ImageService;
import com.web.SellShoes.service.ProductService;
import com.web.SellShoes.service.VariantService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/product")
public class ProductController {
	private final ProductService productService;
	private final Mapper mapper;
	private final ImageService imageService;
	private final VariantService variantService;

	@GetMapping(value = "/admin")
	public String getProductView(HttpSession session, Model model) {
		model.addAttribute("fullName",(String) session.getAttribute("fullName"));
		return "admin/product/add";
	}

	@PostMapping(value = "/add", consumes = "multipart/form-data", produces = { "application/json", "application/xml" })
	@Transactional
	@ResponseBody
	public ResponseEntity<?> saveProduct(@RequestParam(value = "file", required = false) MultipartFile[] files,
			@RequestParam(required = false, defaultValue = "0") int defaultFileIndex,
			@Valid @ModelAttribute ProductRequestDto productRequestDto, BindingResult bindingResult) {

		Map<String, Object> errors = new HashMap<>();
		if (bindingResult.hasErrors()) {
			errors.put("bindingErrors", bindingResult.getAllErrors());
		}
		if (files == null || files.length == 0) {
			errors.put("fileErrors", "Please select at least 1 photo!");
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

	@GetMapping(value = "/getProductPage")
	@ResponseBody
	public ResponseEntity<ProductPageResponseDto> getProductPage(@RequestParam(defaultValue = "7") int size,
			@RequestParam(defaultValue = "0") int page, @RequestParam(required = false) String keyword) {
		Page<Product> productPage = null;
		if (keyword.equals("")) {
			productPage = productService.getAllProduct(page, size);
		} else {
			productPage = productService.getProductByKey(page, size, keyword);
		}

		List<ProductResponseDto> productResponseDtos = productPage.stream()
				.map(product -> new ProductResponseDto(product.getId(), product.getTitle(), product.getDiscription(),
						product.getCreateAt(), product.getUpdateAt(), product.getCategory().getCategoryName(),
						product.getBrand().getName(), imageService.getImageByProduct(product)))
				.collect(Collectors.toList());

		ProductPageResponseDto productPageResponseDto = new ProductPageResponseDto(productPage.getTotalPages(),
				productPage.getNumber(), productPage.getSize(), productResponseDtos);

		return ResponseEntity.ok(productPageResponseDto);
	}

	@GetMapping("/getProduct")
	public ResponseEntity<?> getProduct(@RequestParam Integer id) {
		Optional<Product> productById = productService.getProductById(id);
		ProductResponseDto productResponseDto = null;
		if (productById.isPresent()) {
			productResponseDto = new ProductResponseDto(id, productById.get().getTitle(),
					productById.get().getDiscription(), productById.get().getCategory().getId(),
					productById.get().getBrand().getId(), imageService.getImageByProduct(productById.get()));
		}

		return ResponseEntity.ok(productResponseDto);
	}

	@PutMapping(value = "/update", consumes = "multipart/form-data", produces = { "application/json",
			"application/xml" })
	@Transactional
	@ResponseBody
	public ResponseEntity<?> updateProduct(@RequestParam(value = "file", required = false) MultipartFile[] files,
			@RequestParam(value = "fileNames", required = false) List<String> fileNames,
			@RequestParam int defaultFileIndex, @Valid @ModelAttribute ProductRequestDto productRequestDto,
			BindingResult bindingResult) {
		Map<String, Object> errors = new HashMap<>();

		if (bindingResult.hasErrors()) {
			errors.put("bindingErrors", bindingResult.getAllErrors());
		}
		if (files == null && fileNames == null) {
			errors.put("fileErrors", "Please select at least 1 photo!");
		}
		Optional<Product> productByTitle = productService.getProductByTitle(productRequestDto.getTitle());

		if (productByTitle.isPresent() && !productByTitle.get().getId().equals(productRequestDto.getId())) {
			errors.put("titleDuplicate", "Title already exists! please enter a new title. ");
		}

		if (!productService.getProductById(productRequestDto.getId()).isPresent()) {
			errors.put("NotFoundProduct", "This product does not exist! Update failed.");
		}

		if (!errors.isEmpty()) {
			return ResponseEntity.badRequest().body(errors);
		}

		Product product = mapper.productRequestDtoToProduct(productRequestDto);
		product.setUpdateAt(LocalDate.now());
		productService.save(product);

		imageService.deleteByProduct(product);

		if (files != null) {
			imageService.uploadFile(files, defaultFileIndex, product);
		}
		if (fileNames != null) {
			imageService.uploadFileList(fileNames, defaultFileIndex, product);
		}

		return ResponseEntity.ok().body("{\"message\": \"Update product successfully\"}");
	}

	@DeleteMapping(value = "/delete")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> deleteProduct(@RequestParam("productId") Integer productId) {

		// check if category is exist
		Optional<Product> productById = productService.getProductById(productId);
		if (productById.isEmpty() || productById.get().getDeleteAt() != null) {

			return ResponseEntity.badRequest().body("Product is not exist! Delete failse!");
		}

		List<Variant> variants = variantService.getVariantsByProductId(productId);
		if (!variants.isEmpty()) {
			return ResponseEntity.badRequest().body("There are designs in the product that cannot be erased!!");
		}

		productService.deleteProduct(productById.get());
		imageService.deleteByProduct(productById.get());

		return ResponseEntity.ok().body("Product deleted successfully.");
	}

	

}
