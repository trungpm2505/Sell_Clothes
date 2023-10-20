package com.web.SellShoes.dto.responseDto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VariantResponseDto {
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
