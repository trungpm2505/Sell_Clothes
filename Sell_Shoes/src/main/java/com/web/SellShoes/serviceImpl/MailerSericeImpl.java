package com.web.SellShoes.serviceImpl;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.web.SellShoes.dto.requestDto.MailInfoDto;
import com.web.SellShoes.entity.ConfirmationToken;
import com.web.SellShoes.entity.User;
import com.web.SellShoes.service.ConfirmationTokenService;
import com.web.SellShoes.service.MailerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailerSericeImpl implements MailerService {
	private final JavaMailSender javaMailSender;
	private final ConfirmationTokenService confirmationTokenService;

	@Override
	public void send(MailInfoDto mail) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(mail.getTo());
		msg.setSubject(mail.getSubject());
		msg.setText(mail.getBody());

		javaMailSender.send(msg);
	}

	Random random = new Random();

	@Override
	public void sendEmailToConfirmAccount(User user) {
		ConfirmationToken confirmationToken = new ConfirmationToken(UUID.randomUUID().toString(), user,
				LocalDateTime.now().plusMinutes(2));
		confirmationTokenService.save(confirmationToken);
		MailInfoDto mailInfoDto = new MailInfoDto(user.getEmail(), "Verify Your Account",
				"Thank you for signing up for our service. To ensure the security of your account, please verify your email address by clicking on the link below:"
				+ "http://localhost:8080/register/confirm-account?token=" + confirmationToken.getToken()+ "\n\nBest regards!!");
		send(mailInfoDto);
	}

}
