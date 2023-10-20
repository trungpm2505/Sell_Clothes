package com.web.SellShoes.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.web.SellShoes.entity.Color;
import com.web.SellShoes.repository.ColorRepository;
import com.web.SellShoes.service.ColorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ColorServiceImpl implements ColorService {
	private final ColorRepository colorRepository;

	@Override
	public List<Color> getAll() {
		List<Color> colors = colorRepository.findAll();
		return colors;
	}

	@Override
	public Optional<Color> getColor(Integer colorId) {
		Optional<Color> color = colorRepository.getColorById(colorId);
		return color;
	}

}
