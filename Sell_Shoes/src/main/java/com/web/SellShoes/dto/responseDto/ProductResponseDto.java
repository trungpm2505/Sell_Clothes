package com.web.SellShoes.dto.responseDto;

import java.time.LocalDate;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductResponseDto {
	private Integer id;

	private String title;
	
	private String discription;

	private LocalDate createAt ;

	private LocalDate updateAt;
	
	private String categoryName;
	
	private String brandName;
	
	private Integer categoryId;
	
	private Integer brandId;

	private List<ImageResponseDto> images;
	
	private List<VariantResponseDto> variantResponseDtos;

	public ProductResponseDto(Integer id, String title, String discription, LocalDate createAt, LocalDate updateAt,
			String categoryName, String brandName, List<ImageResponseDto> images) {
		super();
		this.id = id;
		this.title = title;
		this.discription = discription;
		this.createAt = createAt;
		this.updateAt = updateAt;
		this.categoryName = categoryName;
		this.brandName = brandName;
		this.images = images;
	}

	public ProductResponseDto(Integer id, String title, String discription, Integer categoryId, Integer brandId,
			List<ImageResponseDto> images) {
		super();
		this.id = id;
		this.title = title;
		this.discription = discription;
		this.categoryId = categoryId;
		this.brandId = brandId;
		this.images = images;
	}

	public ProductResponseDto(Integer id, String title, String discription, List<ImageResponseDto> images,
			List<VariantResponseDto> variantResponseDtos) {
		super();
		this.id = id;
		this.title = title;
		this.discription = discription;
		this.images = images;
		this.variantResponseDtos = variantResponseDtos;
	}
	
	
}
