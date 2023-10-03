package com.web.SellShoes.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.web.SellShoes.entity.Category;
import com.web.SellShoes.repository.CategoryRepository;
import com.web.SellShoes.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{
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

}
