package com.web.SellShoes.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.web.SellShoes.dto.responseDto.AccountResponseDto;
import com.web.SellShoes.dto.responseDto.ResponseResponseDto;
import com.web.SellShoes.entity.Rate;
import com.web.SellShoes.entity.ResponseRate;
import com.web.SellShoes.repository.ResponseRepository;
import com.web.SellShoes.service.ResponseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResponseServiceImpl implements ResponseService {
	private final ResponseRepository responseRepository;

	@Override
	public List<ResponseResponseDto> getAll(Rate rate) {

		List<ResponseRate> responses = responseRepository.getResponsesByRate(rate);
		List<ResponseResponseDto> responseResponseDtos = responses.stream()
				.map(res -> new ResponseResponseDto(res.getId(),
						new AccountResponseDto(res.getAccount().getFullName(),
								res.getAccount().getRole().getRoleName()),
						res.getContent()))
				.collect(Collectors.toList());
		for (ResponseResponseDto responseResponseDto : responseResponseDtos) {
			System.out.println("getAll response" + responseResponseDto.getContent());
		}
		return responseResponseDtos;
	}

	@Override
	public void save(ResponseRate response) {
		responseRepository.save(response);
	}
}
