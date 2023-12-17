package com.web.SellShoes.dto.requestDto;

import java.io.File;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BrandRequesDto {
	private Integer id;
	
	@NotBlank(message = "BrandName cannot empty!")
	@Size(min = 1, max = 50, message = "brandName cannot be empty and must not exceed 50 characters!")
	private String brandName; 
	
	private String descriptionName; 
}
