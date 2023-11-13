package com.web.SellShoes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.web.SellShoes.entity.Color;


public interface ColorService {
	public List<Color> getAll();
	public List<Color> findAll();
	public Optional<Color> getColor(Integer colorId);
	public Optional<Color> findById(Integer id);
	public Optional<Color> getColorById(Integer colorId);
	public Optional<Color> findByColorName(String colorName);
	public Page<Color> getAllColor(int pagenumber, int color);
	public Page<Color> getColorByKey(int pagenumber, int color, String keyWord);
	public Page<Color> findColorPage(Pageable pageable);
	public void delete(Color entity);
	public <S extends Color> S save(S entity);
}
