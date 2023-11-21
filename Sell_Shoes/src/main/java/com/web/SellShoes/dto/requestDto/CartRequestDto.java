package com.web.SellShoes.dto.requestDto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartRequestDto {
	private int quantity;
//	private String size;
//	private String color;
	private LocalDate updatedAt;
	private LocalDate deleteAt;
	private Integer variantId;
	private Integer accountId;
}
