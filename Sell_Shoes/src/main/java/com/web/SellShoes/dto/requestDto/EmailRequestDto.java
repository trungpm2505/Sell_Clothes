package com.web.SellShoes.dto.requestDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmailRequestDto {
	@Size(min=1,max = 100,message="Email cannot be empty and must not exceed 100 characters")
	@Email(message="Email should be valid")
	private String email;
}
