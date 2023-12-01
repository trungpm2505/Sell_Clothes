package com.web.SellShoes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.SellShoes.entity.Size;

@Repository
public interface SizeRepository extends JpaRepository<Size, Integer> {
	@Query("SELECT c FROM Size c WHERE c.deleteAt is null AND c.id = :sizeId")
	public Optional<Size> getSizeById(Integer sizeId);

	@Query("SELECT c FROM Size c WHERE c.deleteAt is null")
	Page<Size> findSizePage(Pageable pageable);

	@Query("SELECT c FROM Size c WHERE c.deleteAt is null AND c.size LIKE %:keyword%")
	Page<Size> findByKeyWord(Pageable pageable, String keyword);

	@Query("SELECT c FROM Size c WHERE c.deleteAt is null AND c.size = :sizeName")
	Optional<Size> findBySizeName(String sizeName);
	
	@Query("SELECT c FROM Size c WHERE c.deleteAt is null")
	List<Size> findAll();
}
