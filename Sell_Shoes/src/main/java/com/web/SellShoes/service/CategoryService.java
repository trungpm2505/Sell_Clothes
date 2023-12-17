package com.web.SellShoes.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import com.web.SellShoes.entity.Category;

public interface CategoryService {

	Optional<Category> findByCategoryName(String categoryName);

	Page<Category> getCategoryByKey(int pagenumber, int size, String keyWord);

	Page<Category> getAllCategory(int pagenumber, int size);

	Page<Category> findCategoryPage(Pageable pageable);

	void delete(Category entity);

	Optional<Category> findById(Integer id);

	Optional<Category> getCategoryById(Integer categoryId);

	List<Category> findAll();

	<S extends Category> S save(S entity);

	List<Category> getAll();

	Optional<Category> getCategory(Integer categoryId);
}
