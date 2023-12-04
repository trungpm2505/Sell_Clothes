package com.web.SellShoes.dto.requestDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CheckTokenResetPass {
	@Size(min=1,max = 100,message="Email cannot be empty and must not exceed 100 characters")
	@Email(message="Email should be valid")
	private String email;
	
	@Pattern(regexp = "\\d{6}", message = "Code has 6 digits.")
	private String token;
}
