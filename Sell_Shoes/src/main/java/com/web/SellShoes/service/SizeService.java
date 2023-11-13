package com.web.SellShoes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.web.SellShoes.entity.Category;
import com.web.SellShoes.entity.Size;

public interface SizeService {
	public List<Size> getAll();
	public List<Size> findAll();
	public Optional<Size> getSize(Integer sizeId);
	public Optional<Size> findById(Integer id);
	public Optional<Size> getSizeById(Integer sizeId);
	public Optional<Size> findBySizeName(String sizeName);
	public Page<Size> getAllSize(int pagenumber, int size);
	public Page<Size> getSizeByKey(int pagenumber, int size, String keyWord);
	public Page<Size> findSizePage(Pageable pageable);
	public void delete(Size entity);
	public <S extends Size> S save(S entity);
}
