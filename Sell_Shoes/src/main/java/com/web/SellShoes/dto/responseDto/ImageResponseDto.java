package com.web.SellShoes.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImageResponseDto {
	private Integer id;

	private String inmageForShow;
	
	private String inmageForSave;
	
	private Boolean isDefault ;
}
