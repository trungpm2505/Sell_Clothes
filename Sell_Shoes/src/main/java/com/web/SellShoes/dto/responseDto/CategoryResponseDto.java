package com.web.SellShoes.dto.responseDto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CategoryResponseDto {
	private Integer id;
	private String categoryName;
	private LocalDate createAt; 
	private LocalDate updateAt;
//	private LocalDate deleteAt;
	public CategoryResponseDto(Integer id, String categoryName) {
		super();
		this.id = id;
		this.categoryName = categoryName;
	}
	public CategoryResponseDto(Integer id, String categoryName, LocalDate createAt, LocalDate updateAt) {
		super();
		this.id = id;
		this.categoryName = categoryName;
		this.createAt = createAt;
		this.updateAt = updateAt;
	//	this.deleteAt = deleteAt;
	}
	
//	
}
