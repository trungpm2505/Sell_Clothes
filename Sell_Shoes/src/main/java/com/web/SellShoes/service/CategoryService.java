package com.web.SellShoes.service;

import java.util.List;
import java.util.Optional;

import com.web.SellShoes.entity.Category;

public interface CategoryService {
	public Optional<Category> getCategory(Integer categoryId);
	public List<Category> getAll();
}
