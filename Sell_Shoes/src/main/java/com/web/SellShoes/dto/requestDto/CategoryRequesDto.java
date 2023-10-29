package com.web.SellShoes.dto.requestDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryRequesDto {
	private Integer id;
	@NotBlank(message = "CategoryName cannot empty!")
	@Size(min = 1, max = 50, message = "categoryName cannot be empty and must not exceed 50 characters!")
	private String categoryName;
}
