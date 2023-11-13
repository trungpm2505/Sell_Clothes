package com.web.SellShoes.dto.requestDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
	
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SizeRequestDto {
	private Integer id;
	@NotBlank(message = "SizeName cannot empty!")
	@Size(min = 1, max = 50, message = "sizeName cannot be empty and must not exceed 3 characters!")
	private String sizeName;
}