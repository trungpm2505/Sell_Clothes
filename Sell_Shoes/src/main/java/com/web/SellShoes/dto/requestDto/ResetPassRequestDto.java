package com.web.SellShoes.dto.requestDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResetPassRequestDto {
	@NotBlank(message = "Password cannot be empty")
	@Size(min = 6, max = 30,message=" Password must be between 6 and 30 characters long")
	private String newPass;
	
	@Size(min = 1,message="Please enter new pasword again.")
	private String newPassAgain;
	
	@Size(min=1,max = 100,message="Email cannot be empty and must not exceed 100 characters")
	@Email(message="Email should be valid")
	private String email;
}
