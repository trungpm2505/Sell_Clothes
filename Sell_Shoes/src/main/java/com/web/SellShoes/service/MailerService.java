package com.web.SellShoes.service;

import com.web.SellShoes.dto.requestDto.MailInfoDto;
import com.web.SellShoes.entity.Account;

public interface MailerService {
	void send(MailInfoDto mail);
	void sendEmailToConfirmAccount(Account account);
	void sendEmailToResetPassword(Account account);
}
