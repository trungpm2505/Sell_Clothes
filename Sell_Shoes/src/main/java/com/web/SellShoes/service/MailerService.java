package com.web.SellShoes.service;

import com.web.SellShoes.dto.requestDto.MailInfoDto;
import com.web.SellShoes.entity.User;

public interface MailerService {
	void send(MailInfoDto mail);
	void sendEmailToConfirmAccount(User user);
	
}
