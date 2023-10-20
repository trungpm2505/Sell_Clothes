package com.web.SellShoes.dto.requestDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VariantRequestDto {
	private Integer variantId;
	
	private Integer product;

	@Min(value = 0, message = "Price cannot be empty and must be a positive number.")
	private float price;

	@Min(value = 0, message = "Curent price must be a positive number.")
	private float currentPrice;

	@Min(value = 0, message = "Quantity must be a positive number.")
	private int quantity;

	@NotNull(message = "Color cannot be empty!")
	private Integer color;

	@NotNull(message = "Size cannot be empty!")
	private Integer size;
	
	private String note;
}
