package com.web.SellShoes.dto.requestDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FeedbackDto {
	private Integer id;
	
	@NotBlank(message="Fullname cannot be empty")
	@Size(max = 60,message="Full name must not exceed 60 characters")
	private String fullName;
	
	@Size(min=1,max = 100,message="Email cannot be empty and must not exceed 100 characters")
	@Email(message="Email should be valid")
	private String email;
	
	@Pattern(regexp = "^\\+?[0-9]{10,12}$", message = "Phone can not empty and should be between 10 to 12 digits")
	private String phone;
	
	@NotBlank(message="Subject Name cannot be empty")
	@Size(max = 60,message="Subject Name must not exceed 60 characters")
	private String subjectName;
	
	@NotBlank(message="Note cannot be empty")
	@Size(max = 1500,message="Note must not exceed 1500 characters")
	private String note;
}
