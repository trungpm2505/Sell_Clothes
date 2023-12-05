package com.web.SellShoes.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import com.web.SellShoes.entity.Brand;

public interface BrandService {

	Optional<Brand> findByBrandName(String brandName);
	
	Optional<Brand> findByDescriptionName(String descriptionName);
	
	Optional<Brand> findByThumbnailName(String thumbnailName);


	Page<Brand> getBrandByKey(int pagenumber, int size, String keyWord);

	Page<Brand> getAllBrand(int pagenumber, int size);

	Page<Brand> findBrandPage(Pageable pageable);

	void delete(Brand entity);

	Optional<Brand> findById(Integer id);

	Optional<Brand> getBrandById(Integer brandId);

	List<Brand> findAll();

	<S extends Brand> S save(S entity);

	List<Brand> getAll();

	Optional<Brand> getBrand(Integer brandId);
//	public Optional<Category> getCategory(Integer categoryId);
//	public List<Category> getAll();
//	<S extends Category> S save(S entity);
//	<S extends Category> List<S> findAll(Example<S> example, Sort sort);
//	<S extends Category> List<S> findAll(Example<S> example);
//	<S extends Category> Page<S> findAll(Example<S> example, Pageable pageable);
//	List<Category> findAll(Sort sort);
//	Page<Category> findAll(Pageable pageable);
//	List<Category> findAll();
//	void deleteAll();
//	Category getReferenceById(Integer id);
//	void deleteAll(Iterable<? extends Category> entities);
//	void deleteAllById(Iterable<? extends Integer> ids);
//	Category getById(Integer id);
//	void delete(Category entity);
//	Category getOne(Integer id);
//	void deleteById(Integer id);
//	void deleteAllInBatch();
//	long count();
//	<S extends Category, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction);
//	void deleteAllByIdInBatch(Iterable<Integer> ids);
//	<S extends Category> boolean exists(Example<S> example);
//	void deleteAllInBatch(Iterable<Category> entities);
//	<S extends Category> long count(Example<S> example);
//	boolean existsById(Integer id);
//	void deleteInBatch(Iterable<Category> entities);
//	Optional<Category> findById(Integer id);
//	<S extends Category> List<S> saveAllAndFlush(Iterable<S> entities);
//	<S extends Category> S saveAndFlush(S entity);
//	void flush();
//	<S extends Category> List<S> saveAll(Iterable<S> entities);
//	<S extends Category> Optional<S> findOne(Example<S> example);
//	Optional<Category> getCategoryById(Integer categoryId);
//	Page<Category> findCategoryPage(Pageable pageable);
//	 Page<Category>getAllCategory(int pagenumber,int size);
//	 Page<Category>getCategoryByKey(int pagenumber,int size,String keyWord);
//	Optional<Category> findByCategoryName(String categoryName);

}
