package com.web.SellShoes.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.web.SellShoes.entity.Category;
import com.web.SellShoes.repository.CategoryRepository;
import com.web.SellShoes.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
	public final CategoryRepository categoryRepository;

	@Override
	public Optional<Category> getCategory(Integer categoryId) {
		Optional<Category> category = categoryRepository.getCategoryById(categoryId);
		return category;
	}

	@Override
	public List<Category> getAll() {
		List<Category> categories = categoryRepository.findAll();
		return categories;
	}

	@Override
	public <S extends Category> S save(S entity) {
		return categoryRepository.save(entity);
	}

	@Override
	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	@Override
	public Optional<Category> getCategoryById(Integer categoryId) {
		return categoryRepository.getCategoryById(categoryId);
	}

	@Override
	public Optional<Category> findById(Integer id) {
		return categoryRepository.findById(id);
	}

	@Override
	public void delete(Category entity) {
		entity.setDeleteAt(LocalDate.now());
		categoryRepository.save(entity);
	}

	@Override
	public Page<Category> findCategoryPage(Pageable pageable) {
		return categoryRepository.findCategoryPage(pageable);
	}

	@Override
	public Page<Category> getAllCategory(int pagenumber, int size) {
		PageRequest categoryPageable = PageRequest.of(pagenumber, size, Sort.by(Sort.Direction.ASC, "categoryName"));

		Page<Category> categoryPage = categoryRepository.findCategoryPage(categoryPageable);
	
	    return categoryPage;
	}

	@Override
	public Page<Category> getCategoryByKey(int pagenumber, int size, String keyWord) {
		PageRequest categoryPageable = PageRequest.of(pagenumber, size, Sort.by(Sort.Direction.ASC, "categoryName"));

		return categoryRepository.findByKeyWord(categoryPageable, keyWord);
	}

	@Override
	public Optional<Category> findByCategoryName(String categoryName) {
		return categoryRepository.findByCategoryName(categoryName);
	}

}
