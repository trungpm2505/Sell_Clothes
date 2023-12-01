package com.web.SellShoes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.SellShoes.entity.Color;

@Repository
public interface ColorRepository extends JpaRepository<Color, Integer> {
	@Query("SELECT c FROM Color c WHERE c.deleteAt is null AND c.id = :colorId")
	public Optional<Color> getColorById(Integer colorId);

	@Query("SELECT c FROM Color c WHERE c.deleteAt is null")
	Page<Color> findColorPage(Pageable pageable);

	@Query("SELECT c FROM Color c WHERE c.deleteAt is null AND c.color LIKE %:keyword%")
	Page<Color> findByKeyWord(Pageable pageable, String keyword);

	@Query("SELECT c FROM Color c WHERE c.deleteAt is null AND c.color = :colorName")
	Optional<Color> findByColorName(String colorName);
	
	@Query("SELECT c FROM Color c WHERE c.deleteAt is null")
	List<Color> findAll();

}
