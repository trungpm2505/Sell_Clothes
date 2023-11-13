package com.web.SellShoes.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.web.SellShoes.entity.Size;
import com.web.SellShoes.repository.SizeRepository;
import com.web.SellShoes.service.SizeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SizeServiceImpl implements SizeService {
	private final SizeRepository sizeRepository;

	@Override
	public List<Size> getAll() {
		List<Size> sizes = sizeRepository.findAll();
		return sizes;
	}

	@Override
	public Optional<Size> getSize(Integer sizeId) {
		Optional<Size> size = sizeRepository.getSizeById(sizeId);
		return size;
	}

	@Override
	public List<Size> findAll() {
		return sizeRepository.findAll();
	}

	@Override
	public Optional<Size> findById(Integer id) {
		return sizeRepository.findById(id);
	}

	@Override
	public Optional<Size> getSizeById(Integer sizeId) {
		return sizeRepository.getSizeById(sizeId);
	}

	@Override
	public Optional<Size> findBySizeName(String sizeName) {
		return sizeRepository.findBySizeName(sizeName);
	}

	@Override
	public void delete(Size entity) {
		entity.setDeleteAt(LocalDate.now());
		sizeRepository.save(entity);
		
	}

	@Override
	public <S extends Size> S save(S entity) {	
		return sizeRepository.save(entity);
	}

	@Override
	public Page<Size> getAllSize(int pagenumber, int size) {
		PageRequest sizePageable = PageRequest.of(pagenumber, size, Sort.by(Sort.Direction.ASC,"size"));
		
		Page<Size> sizePage = sizeRepository.findSizePage(sizePageable);
		return sizePage;
	}

	@Override
	public Page<Size> getSizeByKey(int pagenumber, int size, String keyWord) {
		PageRequest sizePageable = PageRequest.of(pagenumber, size, Sort.by(Sort.Direction.ASC,"size"));
		
		return sizeRepository.findByKeyWord(sizePageable, keyWord);
	}

	@Override
	public Page<Size> findSizePage(Pageable pageable) {
		return sizeRepository.findSizePage(pageable);
	}

}
