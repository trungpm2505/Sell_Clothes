package com.web.SellShoes.dto.requestDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ColorRequestDto {
	private Integer id;
	@NotBlank(message = "ColorName cannot empty!")
	@Size(min = 1, max = 50, message = "colorName cannot be empty and must not exceed 50 characters!")
	private String colorName;
}
