package com.web.SellShoes.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartResponseDto {
	private Integer cartId;

	private int quantity;
	private VariantResponseDto variantResponseDto;
	private ProductResponseDto productResponseDto;
	private Float price;
	private Float currentPrice;
	private Float total;

	private ImageResponseDto images;

}
