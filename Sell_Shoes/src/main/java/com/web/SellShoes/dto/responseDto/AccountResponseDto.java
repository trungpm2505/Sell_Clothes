package com.web.SellShoes.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountResponseDto {
	private Integer id;
	private String fullName;
	private String email;
	private String phone;
	private String address;
	private String roleName;

	public AccountResponseDto(String fullName, String roleName) {
		super();
		this.fullName = fullName;
		this.roleName = roleName;
	}
}
