package com.web.SellShoes.dto.responseDto;

import java.time.LocalDate;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderResponseDto {
	private Integer id;
	private String fullName;
	private String phone;
	private String address;
	private LocalDate createAt ;
	private LocalDate completedAt;
	private float totalMoney;
	private int status;
	private String note;
	private boolean rated;
	List<OrderDetailResponseDto> orderDetaitsResponseDtos;
	
	public OrderResponseDto(Integer id, String fullName, String phone, String address, LocalDate createAt,
			float totalMoney, int status, String note,boolean rate) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.phone = phone;
		this.address = address;
		this.createAt = createAt;
		this.totalMoney = totalMoney;
		this.status = status;
		this.note = note;
		this.rated = rate;
	}
	
	public OrderResponseDto(Integer id, String fullName, String phone, String address, LocalDate createAt,LocalDate completedAt,
			float totalMoney, int status, String note) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.phone = phone;
		this.address = address;
		this.createAt = createAt;
		this.completedAt= completedAt;
		this.totalMoney = totalMoney;
		this.status = status;
		this.note = note;
		
	}
}
