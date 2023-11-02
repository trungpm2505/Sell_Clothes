package com.web.SellShoes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.SellShoes.entity.Color;

@Repository
public interface ColorRepository extends JpaRepository<Color, Integer>{
	@Query("SELECT c FROM Color c WHERE c.deleteAt is null AND c.id = :colorId")
	public Optional<Color> getColorById(Integer colorId);
}
