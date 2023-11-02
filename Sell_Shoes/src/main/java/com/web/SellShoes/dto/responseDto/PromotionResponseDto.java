package com.web.SellShoes.dto.responseDto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PromotionResponseDto {
	private Integer id;

	private String couponCode;

	private String name;

	private int discountType;

	private Long discountValue;

	private Long maxValue;

	private boolean isPublic;

	private boolean active;

	private LocalDate expiredDate;

	private LocalDate createAt;

	private LocalDate updateAt;
}
