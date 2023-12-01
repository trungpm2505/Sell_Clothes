package com.web.SellShoes.dto.requestDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FeedbackDto {
	private Integer id;
	
	@NotBlank(message = "FeedbackFullName cannot empty!")
	@Size(min = 1, max = 50, message = "feedbackFullName cannot be empty and must not exceed 20 characters!")
	private String fullName;
	
	@NotBlank(message = "FeedbackEmail cannot empty!")
	@Size(min = 1, max = 50, message = "feedbackEmail cannot be empty and must not exceed 30 characters!")
	private String email;
	
	
	@NotBlank(message = "FeedbackPhone cannot empty!")
	@Size(min = 1, max = 50, message = "feedbackPhonecannot be empty and must not exceed 13 characters!")
	private String phone;
	
	@NotBlank(message = "FeedbacksubjectName cannot empty!")
	@Size(min = 1, max = 50, message = "feedbacksubjectName cannot be empty and must not exceed 20 characters!")
	private String subjectName;
	
	@NotBlank(message = "FeedbackNote cannot empty!")
	@Size(min = 1, max = 1500, message = "Note cannot be empty and must not exceed 100 characters!")
	private String note;
}
