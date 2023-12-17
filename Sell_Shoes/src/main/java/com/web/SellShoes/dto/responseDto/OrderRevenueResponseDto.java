package com.web.SellShoes.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderRevenueResponseDto {
	private Integer monthId;
	private Double revenue;
}
