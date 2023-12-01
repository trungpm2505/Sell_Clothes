package com.web.SellShoes.dto.responseDto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserFeedbackResponseDto {
	private Integer id;
    private String fullname;
    private String email;
    private String phone;
    private String subjectName;
    private String note; 
    private LocalDate createAt;
}
