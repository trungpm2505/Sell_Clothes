package com.web.SellShoes.dto.requestDto;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangePasswordRequestDto {
	@Size(min = 1, max = 30, message = "please enter curent password")
	private String currentPassword;
	@Size(min = 6, max = 30, message = "Password cannot be empty and must be between 6 and 30 characters long")
	private String newPassword;
	@Size(min = 1, message = "Please enter new pasword again.")
	private String confirmNewPassword;
}
