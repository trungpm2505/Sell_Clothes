package com.web.SellShoes.service;

import java.util.List;
import java.util.Optional;

import com.web.SellShoes.entity.Color;

public interface ColorService {
	public List<Color> getAll();
	public Optional<Color> getColor(Integer colorId);
}
