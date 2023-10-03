package com.web.SellShoes.dto.requestDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductRequestDto {
	private Integer id;

	@NotBlank(message = "Title cannot be empty and must not exceed 50 characters!")
	@Size(min = 1, max = 300, message = "Title cannot be empty and must not exceed 300 characters!")
	private String title;

	@NotBlank(message = "Discription cannot be empty and must not exceed 1500 characters!")
	@Size(min = 1, max = 1500, message = "Discription cannot be empty and must not exceed 1500 characters!")
	private String discription;

	@NotNull(message = "Category cannot be empty!")
	private Integer category;

	@NotNull(message = "Brand cannot be empty!")
	private Integer brand;
}
