package com.web.SellShoes.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

	@Override
	public List<Color> findAll() {
		return colorRepository.findAll();
	}

	@Override
	public Optional<Color> findById(Integer id) {

		return colorRepository.findById(id);
	}

	@Override
	public Optional<Color> getColorById(Integer colorId) {
		return colorRepository.getColorById(colorId);
	}

	@Override
	public Optional<Color> findByColorName(String colorName) {
		return colorRepository.findByColorName(colorName);
	}

	@Override
	public Page<Color> getAllColor(int pagenumber, int color) {
		PageRequest colorPageable = PageRequest.of(pagenumber, color, Sort.by(Sort.Direction.ASC,"color"));
		Page<Color> colorPage = colorRepository.findColorPage(colorPageable);
		return colorPage;
	}

	@Override
	public Page<Color> getColorByKey(int pagenumber, int color, String keyWord) {
		PageRequest colorPageable = PageRequest.of(pagenumber, color, Sort.by(Sort.Direction.ASC,"color"));
		
		return colorRepository.findByKeyWord(colorPageable, keyWord);
	}

	@Override
	public Page<Color> findColorPage(Pageable pageable) {
		return colorRepository.findColorPage(pageable);
	}

	@Override
	public void delete(Color entity) {
		entity.setDeleteAt(LocalDate.now());
		colorRepository.save(entity);
		
	}

	@Override
	public <S extends Color> S save(S entity) {
		return colorRepository.save(entity);
	}

}
