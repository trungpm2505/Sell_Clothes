package com.web.SellShoes.dto.responseDto;

import java.time.LocalDate;

import com.web.SellShoes.entity.Role;

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
	private LocalDate createAt;
	private LocalDate updateAt;
	private boolean active;

	public AccountResponseDto(String fullName, String roleName) {
		super();
		this.fullName = fullName;
		this.roleName = roleName;
	}
	
	public AccountResponseDto(Integer id, String fullName, String address, String email, String phone, LocalDate createAt, LocalDate updateAt, String roleName, boolean active) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.address = address;
		this.email = email;
		this.phone = phone; 
		this.createAt = createAt;
		this.updateAt = updateAt;
		this.roleName = roleName;
		this.active = active;
	}
}
