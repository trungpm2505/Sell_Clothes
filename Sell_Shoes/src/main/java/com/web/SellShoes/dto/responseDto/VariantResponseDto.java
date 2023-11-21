package com.web.SellShoes.dto.responseDto;

import java.time.LocalDate;

import com.web.SellShoes.entity.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VariantResponseDto {
	public VariantResponseDto(Integer id, Integer productId, Integer sizeId, Integer colorId, String size, String color,
			Float price, Float currentPrice, int quantity, int buyCount) {
		super();
		this.id = id;
		this.productId = productId;
		this.sizeId = sizeId;
		this.colorId = colorId;
		this.size = size;
		this.color = color;
		this.price = price;
		this.currentPrice = currentPrice;
		this.quantity = quantity;
		this.buyCount = buyCount;
	}

	private Integer id;

	private Integer productId;
	
	private Integer sizeId;
	
	private Integer colorId;

	private String title;

	private String discription;

	private String size;

	private String color;

	private Float price;

	private Float currentPrice;

	private int quantity;

	private int buyCount;

	private LocalDate createAt;

	private LocalDate updateAt;
}
