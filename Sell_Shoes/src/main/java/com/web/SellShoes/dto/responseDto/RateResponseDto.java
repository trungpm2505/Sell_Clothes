package com.web.SellShoes.dto.responseDto;

import java.time.LocalDateTime;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RateResponseDto {
	private Integer id;

	private LocalDateTime createAt;

	private int rating;

	private String content;

	private AccountResponseDto userResponseDto;

	private List<ResponseResponseDto> responses;

	private List<ImageResponseDto> images;
}
