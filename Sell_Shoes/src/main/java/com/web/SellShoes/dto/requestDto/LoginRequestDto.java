package com.web.SellShoes.dto.requestDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequestDto {
	@NotEmpty(message="Email cannot be empty")
	@Email(message="Email should be valid")
	private String email;

	@NotEmpty(message="Password cannot be empty")
	//@Size(min = 1, max = 30,message="Password cannot be empty")
	private String password;
}
