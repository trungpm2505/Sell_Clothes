package com.web.SellShoes.dto.requestDto;

import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RateRequestDto {
	private Integer rateId;
	@Min(value = 1, message = "Variant cannot be empty.")
	private Integer variantId;
	@Min(value = 1, message = "Rate cannot be empty.")
	private int rating;
	private Integer orderId;
	private String content;
}
