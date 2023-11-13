package com.web.SellShoes.dto.responseDto;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

//@AllArgsConstructor
@NoArgsConstructor
@Data
public class SizeResponseDto {
	private Integer id;
	private String name;
	private LocalDate createAt; 
	private LocalDate updateAt;
	public SizeResponseDto(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public SizeResponseDto(Integer id, String name, LocalDate createAt, LocalDate updateAt) {
		super();
		this.id = id;
		this.name = name;
		this.createAt = createAt;
		this.updateAt = updateAt;
	}	
}
