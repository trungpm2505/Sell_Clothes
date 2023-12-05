package com.web.SellShoes.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.web.SellShoes.dto.requestDto.BrandRequesDto;
import com.web.SellShoes.dto.responseDto.BrandResponseDto;
import com.web.SellShoes.dto.responseDto.BrandPageResponseDto;

import com.web.SellShoes.entity.Brand;

import com.web.SellShoes.service.BrandService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/brand")
public class BrandController {
	private final BrandService brandService;
	private static String UPLOADED_FOLDER = System.getProperty("user.dir") + "//src//main//resources//static//upload//";
	
	@GetMapping(value = "/getAll")
	public ResponseEntity<?> getAllBrand() {
		List<Brand> brands = brandService.getAll();

		List<BrandResponseDto> brandResponseDtos = brands.stream()
				.map(brand -> new BrandResponseDto(brand.getId(), brand.getName())).collect(Collectors.toList());

		return ResponseEntity.ok(brandResponseDtos);
	}

	@GetMapping()
	public String list(Model model) {

		return "admin/brand/brand";
	}

	@GetMapping("/getBrandPage")
	public ResponseEntity<BrandPageResponseDto> getBrandPage(@RequestParam(defaultValue = "8") int size,
			@RequestParam(defaultValue = "0") int page, @RequestParam(required = false) String keyword // Đọc tham số
																										// page từ URL
	) {
		keyword = "";
		Page<Brand> brandPage = null;
		if (keyword == null || keyword.isEmpty()) {
			brandPage = brandService.getAllBrand(page, size);
		} else {
			brandPage = brandService.getBrandByKey(page, size, keyword);
		}
		List<BrandResponseDto> brandResponseDtos = brandPage.stream().map(brand -> new BrandResponseDto(brand.getId(),
				brand.getName(), brand.getCreateAt(), brand.getUpdateAt(), brand.getThumbnail()))
				.collect(Collectors.toList());
		BrandPageResponseDto brandPageResponseDto = new BrandPageResponseDto(brandPage.getTotalPages(),
				brandPage.getNumber(), brandPage.getSize(), brandResponseDtos);
		return ResponseEntity.ok(brandPageResponseDto);
	}

	@PostMapping(value = "/saveBrands", consumes = "multipart/form-data", produces = { "application/json",
			"application/xml" })
	@Transactional
	@ResponseBody
	public ResponseEntity<?> saveBrand(@RequestParam(value = "thumbnailFile", required = false) MultipartFile thumbnailFile,
			@Valid @ModelAttribute BrandRequesDto brandRequesDto, BindingResult bindingResult, String filePath) {

		System.out.println("img: " + thumbnailFile);

		Map<String, Object> errors = new HashMap<>();
		if (bindingResult.hasErrors()) {
			errors.put("bindingErrors", bindingResult.getAllErrors());
			return ResponseEntity.badRequest().body(errors);
		}
		
		if (thumbnailFile == null) {
			errors.put("thumbnailFile", "Please select a file.");
			return ResponseEntity.badRequest().body(errors);
		}

		// Kiểm tra xem tên thương hiệu đã tồn tại chưa
		Optional<Brand> existingBrand = brandService.findByBrandName(brandRequesDto.getBrandName());
		if (existingBrand.isPresent()) {
			// Nếu tên thương hiệu đã tồn tại thì trả về thông báo lỗi
			errors.put("nameDuplicate", "Brand name already exists");
			return ResponseEntity.badRequest().body(errors);
		}

		Brand brand = new Brand();
		try {

			byte[] bytes = thumbnailFile.getBytes();// Lấy dữ liệu của tệp trong dạng mảng byte.

			String randomFileName = UUID.randomUUID().toString() + "."
					+ FilenameUtils.getExtension(thumbnailFile.getOriginalFilename());
			Path dir = Paths.get(UPLOADED_FOLDER);
			Path path = Paths.get(UPLOADED_FOLDER + randomFileName);
			// Tạo thư mục nếu nó chưa tồn tại.
			if (!Files.exists(dir)) {

				Files.createDirectories(dir);
			}
			Files.write(path, bytes);

			// Tạo đối tượng Brand từ dữ liệu DTO
			brand.setName(brandRequesDto.getBrandName());
			brand.setThumbnail(filePath); // Lưu đường dẫn của file thumbnail
			brandService.save(brand);
			// Trả về một thông báo thành công
			return ResponseEntity.ok().body(Map.of("message", "Brand saved successfully!"));

		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Failed to upload file.");
		}
	}

	@GetMapping("/deleteBrand")
	public ResponseEntity<String> deleteBrand(ModelMap model, @RequestParam("idBrand") int idBrand) {
		Optional<Brand> entity = brandService.findById(idBrand);

		if (entity.isPresent()) {
			Brand brand = entity.get();

			// Kiểm tra xem danh mục có sản phẩm nào không
			if (brand.getProducts().isEmpty()) {
				// Nếu không có sản phẩm liên quan, thực hiện xóa danh mục
				brandService.delete(brand);
				return ResponseEntity.ok("Delete successful");
			} else {
				// Nếu danh mục có sản phẩm liên quan, trả về một thông báo hoặc mã lỗi tùy theo
				// yêu cầu của bạn
				return ResponseEntity.badRequest().body("Brand contains associated products and cannot be deleted");
			}
		}

		return ResponseEntity.notFound().build();
	}

	/*
	 * @PostMapping("/editBrand")
	 * 
	 * public ResponseEntity<?> editBrand(@Valid @RequestBody BrandRequesDto
	 * brandRequesDto, BindingResult bindingResult) { Map<String, Object> errors =
	 * new HashMap<>();
	 * 
	 * if (bindingResult.hasErrors()) { errors.put("bindingErrors",
	 * bindingResult.getAllErrors()); return
	 * ResponseEntity.badRequest().body(errors); }
	 * 
	 * Optional<Brand> existingBrand =
	 * brandService.findByBrandName(brandRequesDto.getBrandName());
	 * 
	 * if (existingBrand.isPresent() &&
	 * !existingBrand.get().getId().equals(brandRequesDto.getId())) {
	 * errors.put("nameDuplicate",
	 * "BrandName already exists! Please enter a new BrandName."); return
	 * ResponseEntity.badRequest().body(errors); }
	 * 
	 * if (!brandService.getBrandById(brandRequesDto.getId()).isPresent()) {
	 * errors.put("nameDuplicate", "This brand does not exist! Update failed");
	 * return ResponseEntity.badRequest().body(errors); } Brand entity = new
	 * Brand(); entity.setId(brandRequesDto.getId());
	 * entity.setName(brandRequesDto.getBrandName());
	 * entity.setDescription(brandRequesDto.getDescriptionName());
	 * //entity.setThumbnail(brandRequesDto.getThumbnail());
	 * entity.setUpdateAt(LocalDate.now()); brandService.save(entity);
	 * 
	 * return ResponseEntity.ok("Edit brand successfully"); }
	 */

}
