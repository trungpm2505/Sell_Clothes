package com.web.SellShoes.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
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
	@Override
	public <S extends Category> S save(S entity) {
		return categoryRepository.save(entity);
	}
	@Override
	public List<Category> findAll() {
		return categoryRepository.findAll();
	}
//	@Override
//	public Page<Category> findAll(Pageable pageable) {
//		return categoryRepository.findAll(pageable);
//	}
//	@Override
//	public List<Category> findAll(Sort sort) {
//		return categoryRepository.findAll(sort);
//	}
//	@Override
//	public <S extends Category> Page<S> findAll(Example<S> example, Pageable pageable) {
//		return categoryRepository.findAll(example, pageable);
//	}
//	@Override
//	public <S extends Category> List<S> findAll(Example<S> example) {
//		return categoryRepository.findAll(example);
//	}
//	@Override
//	public <S extends Category> List<S> findAll(Example<S> example, Sort sort) {
//		return categoryRepository.findAll(example, sort);
//	}
	@Override
	public Optional<Category> getCategoryById(Integer categoryId) {
		return categoryRepository.getCategoryById(categoryId);
	}
//	@Override
//	public <S extends Category> Optional<S> findOne(Example<S> example) {
//		return categoryRepository.findOne(example);
//	}
//	public List<Category> findAllById(Iterable<Integer> ids) {
//		return categoryRepository.findAllById(ids);
//	}
//	@Override
//	public <S extends Category> List<S> saveAll(Iterable<S> entities) {
//		return categoryRepository.saveAll(entities);
//	}
//	@Override
//	public void flush() {
//		categoryRepository.flush();
//	}
//	@Override
//	public <S extends Category> S saveAndFlush(S entity) {
//		return categoryRepository.saveAndFlush(entity);
//	}
//	@Override
//	public <S extends Category> List<S> saveAllAndFlush(Iterable<S> entities) {
//		return categoryRepository.saveAllAndFlush(entities);
//	}
	@Override
	public Optional<Category> findById(Integer id) {
		return categoryRepository.findById(id);
	}
//	@Override
//	public void deleteInBatch(Iterable<Category> entities) {
//		categoryRepository.deleteInBatch(entities);
//	}
//	@Override
//	public boolean existsById(Integer id) {
//		return categoryRepository.existsById(id);
//	}
//	@Override
//	public <S extends Category> long count(Example<S> example) {
//		return categoryRepository.count(example);
//	}
//	@Override
//	public void deleteAllInBatch(Iterable<Category> entities) {
//		categoryRepository.deleteAllInBatch(entities);
//	}
//	@Override
//	public <S extends Category> boolean exists(Example<S> example) {
//		return categoryRepository.exists(example);
//	}
//	@Override
//	public void deleteAllByIdInBatch(Iterable<Integer> ids) {
//		categoryRepository.deleteAllByIdInBatch(ids);
//	}
//	@Override
//	public <S extends Category, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
//		return categoryRepository.findBy(example, queryFunction);
//	}
//	@Override
//	public long count() {
//		return categoryRepository.count();
//	}
//	@Override
//	public void deleteAllInBatch() {
//		categoryRepository.deleteAllInBatch();
//	}
//	@Override
//	public void deleteById(Integer id) {
//		categoryRepository.deleteById(id);
//	}
//	@Override
//	public Category getOne(Integer id) {
//		return categoryRepository.getOne(id);
//	}
	@Override
	public void delete(Category entity) {
		entity.setDeleteAt(LocalDate.now());
		categoryRepository.save(entity);
	}
//	@Override
//	public Category getById(Integer id) {
//		return categoryRepository.getById(id);
//	}
//	@Override
//	public void deleteAllById(Iterable<? extends Integer> ids) {
//		categoryRepository.deleteAllById(ids);
//	}
//	@Override
//	public void deleteAll(Iterable<? extends Category> entities) {
//		categoryRepository.deleteAll(entities);
//	}
//	@Override
//	public Category getReferenceById(Integer id) {
//		return categoryRepository.getReferenceById(id);
//	}
//	@Override
//	public void deleteAll() {
//		categoryRepository.deleteAll();
//	}
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
		PageRequest categoryPageable = PageRequest.of(pagenumber, size,Sort.by(Sort.Direction.ASC,"categoryName"));

		return categoryRepository.findByKeyWord(categoryPageable, keyWord);
	}
	@Override
	public Optional<Category> findByCategoryName(String categoryName) {
		return categoryRepository.findByCategoryName(categoryName);
	}
   

	
}
