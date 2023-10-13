package com.web.SellShoes.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


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
	public String list(Model model) {

		return "admin/categories/add";
	}

	@GetMapping(value = "/getAll")
	public ResponseEntity<?> getAllCategory(Model model) {
		List<Category> categories = categoryService.getAll();
		List<CategoryResponseDto> categoryResponseDtos = categories.stream()
				.map(category -> new CategoryResponseDto(category.getId(),
						category.getCategoryName()))
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(categoryResponseDtos);
	}
//@GetMapping("/getCategories")
//public ResponseEntity<List<Category>>getCategories(ModelMap model){
//	List<Category>listCotegory=categoryService.findAll();
//	return new ResponseEntity<>(listCotegory,HttpStatus.OK);
//}
//	 @GetMapping("/getCategories")
//	    public ResponseEntity<Page<Category>> getCategories(Pageable pageable) {
//	        Page<Category> categoryPage = categoryService.findCategoryPage(pageable);
//	        return new ResponseEntity<>(categoryPage, HttpStatus.OK);
//	    }
	@GetMapping("/getCategories")
	public ResponseEntity<Page<Category>> getCategories(@RequestParam(required = false) String keyword, Pageable pageable) {
	    Page<Category> categoryPage;

	    if (keyword != null && !keyword.isEmpty()) {
	        // Nếu có keyword, thực hiện tìm kiếm theo keyword
	        categoryPage = categoryService.getCategoryByKey(pageable.getPageNumber(), pageable.getPageSize(), keyword);
	    } else {
	        // Nếu không có keyword, lấy tất cả danh mục
	        categoryPage = categoryService.getAllCategory(pageable.getPageNumber(), pageable.getPageSize());
	    }

	    return new ResponseEntity<>(categoryPage, HttpStatus.OK);
	}

	
	@RequestMapping("/saveCategory")
	public ResponseEntity<String> comment(
			@RequestParam(name = "categoryName") String categoryName, ModelMap model) {	
		System.out.println("----");
		Category category = new Category();			
		category.setCategoryName(categoryName);
		categoryService.save(category);
		return ResponseEntity.ok("Success");
	}
	@GetMapping("/deleteCategory")
	public ResponseEntity<String>deleteCategory(ModelMap model,@RequestParam("idCategory")int idCategory){
		
		Optional<Category> entity=categoryService.findById(idCategory);
		if (entity.isPresent()) {
			Category category=entity.get();
			categoryService.delete(category);
			categoryService.save(category);
		}

		return ResponseEntity.ok("Success");
	}
   @PostMapping("/editCategory")
   public ResponseEntity<String>editCategory(ModelMap model,@RequestParam("idCategory")int idCategory,
		   @RequestParam("categoryName")String categoryName){

	   LocalDate currentDate = LocalDate.now();
	   // Tìm danh mục dựa trên id
	    Optional<Category> categoryToUpdate = categoryService.findById(idCategory);
	    System.out.println("_-----------"+idCategory);
	    if(categoryToUpdate.isPresent()) {
	    	Category entity=categoryToUpdate.get();
	    	entity.setId(idCategory);
	    	entity.setCategoryName(categoryName);
	    	entity.setUpdateAt(currentDate);
	    categoryService.save(entity);
	    	
	    }
		return ResponseEntity.ok("Success");

}
//   @GetMapping("/searchCategory")
//   public ResponseEntity<List<Category>>searchCategory(ModelMap model,@RequestParam("categoryName")String categoryName)
//   {
//   	List<Category>categories=categoryService.findBycategoryName(categoryName);
//   	return new ResponseEntity<>(categories,HttpStatus.OK);  
//   	}
//   
}
