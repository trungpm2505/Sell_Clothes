package com.web.SellShoes.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.web.SellShoes.dto.requestDto.CategoryRequesDto;
import com.web.SellShoes.dto.responseDto.CategoryPageResponseDto;
import com.web.SellShoes.dto.responseDto.CategoryResponseDto;
import com.web.SellShoes.entity.Category;
import com.web.SellShoes.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/category")
public class CategoryController {
	private final CategoryService categoryService;

	@GetMapping()
	public String list(HttpSession session,Model model) {
		model.addAttribute("fullName",(String) session.getAttribute("fullName"));
		return "admin/categories/add";
	}

	@GetMapping(value = "/getAll")
	public ResponseEntity<?> getAllCategory(Model model) {
		List<Category> categories = categoryService.getAll();
		List<CategoryResponseDto> categoryResponseDtos = categories.stream()
				.map(category -> new CategoryResponseDto(category.getId(), category.getCategoryName()))
				.collect(Collectors.toList());

		return ResponseEntity.ok(categoryResponseDtos);
	}

	@GetMapping("/getCategoryPage")
	public ResponseEntity<CategoryPageResponseDto> getCategoryPage(@RequestParam(defaultValue = "8") int size,
			@RequestParam(defaultValue = "0") int page, @RequestParam(required = false) String keyword // Đọc tham số
																										// page từ URL
	) {

		Page<Category> categoryPage = null;
		if (keyword == null || keyword.isEmpty()) {
			categoryPage = categoryService.getAllCategory(page, size);
		} else {
			categoryPage = categoryService.getCategoryByKey(page, size, keyword);
		}
		List<CategoryResponseDto> categoryResponseDtos = categoryPage.stream()
				.map(category -> new CategoryResponseDto(category.getId(), category.getCategoryName(),
						category.getCreateAt(), category.getUpdateAt()))
				.collect(Collectors.toList());
		CategoryPageResponseDto categoryPageResponseDto = new CategoryPageResponseDto(categoryPage.getTotalPages(),
				categoryPage.getNumber(), categoryPage.getSize(), categoryResponseDtos);
		return ResponseEntity.ok(categoryPageResponseDto);
	}

	@PostMapping("/saveCategory")
	@ResponseBody
	public ResponseEntity<?> saveCategory(@Valid @RequestBody CategoryRequesDto categoryRequesDto,
			BindingResult bindingResult) {
		Map<String, Object> errors = new HashMap<>();
		if (bindingResult.hasErrors()) {
			errors.put("bindingErrors", bindingResult.getAllErrors());
		}

		Optional<Category> existingCategory = categoryService.findByCategoryName(categoryRequesDto.getCategoryName());

		if (existingCategory.isPresent()) {
			// Nếu tên danh mục đã tồn tại thì trả về thông báo lỗi
			errors.put("nameDuplicate", "Category name already exists");
		}
		if (!errors.isEmpty()) {
			return ResponseEntity.badRequest().body(errors);
		}

		Category category = new Category();
		category.setCategoryName(categoryRequesDto.getCategoryName());
		categoryService.save(category);

		// return ResponseEntity.ok("Success");
		return ResponseEntity.ok().body("Add categories successfully");
	}

	@GetMapping("/deleteCategory")
	public ResponseEntity<String> deleteCategory(ModelMap model, @RequestParam("idCategory") int idCategory) {
		Optional<Category> entity = categoryService.findById(idCategory);

		if (entity.isPresent()) {
			Category category = entity.get();

			// Kiểm tra xem danh mục có sản phẩm nào không
			if (category.getProducts().isEmpty()) {
				// Nếu không có sản phẩm liên quan, thực hiện xóa danh mục
				categoryService.delete(category);
				return ResponseEntity.ok("Delete successful");
			} else {
				// Nếu danh mục có sản phẩm liên quan, trả về một thông báo hoặc mã lỗi tùy theo
				// yêu cầu của bạn
				return ResponseEntity.badRequest().body("Category contains associated products and cannot be deleted");
			}
		}

		return ResponseEntity.notFound().build();
	}

	@PostMapping("/editCategory")

	public ResponseEntity<?> editCategory(@Valid @RequestBody CategoryRequesDto categoryRequesDto,
			BindingResult bindingResult) {
		Map<String, Object> errors = new HashMap<>();

		if (bindingResult.hasErrors()) {
			errors.put("bindingErrors", bindingResult.getAllErrors());
			return ResponseEntity.badRequest().body(errors);
		}

		Optional<Category> existingCategory = categoryService.findByCategoryName(categoryRequesDto.getCategoryName());

		if (existingCategory.isPresent() && !existingCategory.get().getId().equals(categoryRequesDto.getId())) {
			errors.put("nameDuplicate", "CategoryName already exists! Please enter a new CategoryName.");
			return ResponseEntity.badRequest().body(errors);
		}

		if (!categoryService.getCategoryById(categoryRequesDto.getId()).isPresent()) {
			errors.put("nameDuplicate", "This category does not exist! Update failed");
			return ResponseEntity.badRequest().body(errors);
		}
		Category entity = new Category();
		entity.setId(categoryRequesDto.getId());
		entity.setCategoryName(categoryRequesDto.getCategoryName());
		entity.setUpdateAt(LocalDate.now());
		categoryService.save(entity);

		return ResponseEntity.ok("Edit categories successfully");
	}

}
