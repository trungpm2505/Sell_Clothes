package com.web.SellShoes.dto.responseDto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryResponseDto {
	private Integer id;
	private String categoryName;
	private LocalDate createAt; 
	private LocalDate updateAt;
	private LocalDate deleteAt;
}
