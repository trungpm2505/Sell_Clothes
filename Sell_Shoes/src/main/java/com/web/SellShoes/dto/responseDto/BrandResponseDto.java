package com.web.SellShoes.dto.responseDto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BrandResponseDto {
	private Integer id;
	private String brandName;
	private LocalDate createAt;
	private LocalDate updateAt;
	private String thumbnail;
	private String description;
	

//	private LocalDate deleteAt;
	public BrandResponseDto(Integer id, String brandName) {
		super();
		this.id = id;
		this.brandName = brandName;
	}

	public BrandResponseDto(Integer id, String brandName, LocalDate createAt, LocalDate updateAt) {
		super();
		this.id = id;
		this.brandName = brandName;
		this.createAt = createAt;
		this.updateAt = updateAt;
		
		// this.deleteAt = deleteAt;
	}

//	
}
