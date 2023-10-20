package com.web.SellShoes.service;

import java.util.List;
import java.util.Optional;

import com.web.SellShoes.entity.Size;

public interface SizeService {
	public List<Size> getAll();
	public Optional<Size> getSize(Integer categoryId);
}
