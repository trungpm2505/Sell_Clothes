package com.web.SellShoes.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetailResponseDto {

	private float price;

	private float curentPrice;

	private int number;

	private float totalMoney;

	private VariantResponseDto variantResponseDto;

	private String imageForSave;

	public OrderDetailResponseDto(float price, int number, float totalMoney, VariantResponseDto variantName) {
		super();
		this.price = price;
		this.number = number;
		this.totalMoney = totalMoney;
		this.variantResponseDto = variantName;
	}
}
