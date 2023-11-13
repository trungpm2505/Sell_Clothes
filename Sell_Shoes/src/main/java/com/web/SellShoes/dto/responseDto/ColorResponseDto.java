package com.web.SellShoes.dto.responseDto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@AllArgsConstructor
@NoArgsConstructor
@Data
public class ColorResponseDto {
	private Integer id;
	private String name;
	private LocalDate createAt; 
	private LocalDate updateAt;
	
	public ColorResponseDto(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public ColorResponseDto(Integer id, String name, LocalDate createAt, LocalDate updateAt) {
		super();
		this.id = id;
		this.name = name;
		this.createAt = createAt;
		this.updateAt = updateAt;
	}
	
}
