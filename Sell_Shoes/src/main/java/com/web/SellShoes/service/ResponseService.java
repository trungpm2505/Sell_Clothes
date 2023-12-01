package com.web.SellShoes.service;

import java.util.List;

import com.web.SellShoes.dto.responseDto.ResponseResponseDto;
import com.web.SellShoes.entity.Rate;
import com.web.SellShoes.entity.ResponseRate;

public interface ResponseService {
	public List<ResponseResponseDto> getAll(Rate rate);

	public void save(ResponseRate response);
}
