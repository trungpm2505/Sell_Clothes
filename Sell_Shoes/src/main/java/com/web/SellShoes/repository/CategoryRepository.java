package com.web.SellShoes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.SellShoes.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	@Query("SELECT c FROM Category c WHERE c.deleteAt is null AND c.id = :categoryId")
	public Optional<Category> getCategoryById(Integer categoryId);

	@Query("SELECT c FROM Category c WHERE c.deleteAt is null")
	Page<Category> findCategoryPage(Pageable pageable);

	@Query("SELECT c FROM Category c WHERE c.deleteAt is null AND c.categoryName LIKE %:keyword%")
	Page<Category> findByKeyWord(Pageable pageable, String keyword);

	@Query("SELECT c FROM Category c WHERE c.deleteAt is null AND c.categoryName = :categoryName")
	Optional<Category> findByCategoryName(String categoryName);
	
	@Query("SELECT c FROM Category c WHERE c.deleteAt is null")
	List<Category> findAll();

}
