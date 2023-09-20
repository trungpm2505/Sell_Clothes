package com.web.SellShoes.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MailInfoDto {
	String to;
	String subject;
	String body;
}
