package com.web.SellShoes.serviceImpl;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.web.SellShoes.dto.requestDto.MailInfoDto;
import com.web.SellShoes.entity.ConfirmationToken;
import com.web.SellShoes.entity.Account;
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
	public void sendEmailToConfirmAccount(Account user) {
		ConfirmationToken confirmationToken = new ConfirmationToken(UUID.randomUUID().toString(), user,
				LocalDateTime.now().plusMinutes(2));
		confirmationTokenService.save(confirmationToken);
		MailInfoDto mailInfoDto = new MailInfoDto(user.getEmail(), "Verify Your Account",
				"Thank you for signing up for our service. To ensure the security of your account, please verify your email address by clicking on the link below:\n"
				+ "http://localhost:8080/register/confirm-account?token=" + confirmationToken.getToken()+ "\n\nBest regards!!");
		send(mailInfoDto);
	}

	@Override
	public void sendEmailToResetPassword(Account account) {
		ConfirmationToken confirmationToken = new ConfirmationToken(String.valueOf(random.nextInt(900000) + 100000),account,LocalDateTime.now().plusMinutes(2));
		confirmationTokenService.save(confirmationToken);
		MailInfoDto mailInfoDto = new MailInfoDto(account.getEmail(), "ADELA-Reset Your Password",
				"Dear "+account.getFullName()+",\r\n"
				+ "\r\n"
				+ "You have requested to reset the password for your account. Below is the confirmation code to complete the password reset process:\r\n"
				+ "\r\n"
				+ "Confirmation Code:" + confirmationToken.getToken()
				+ "\r\n"
				+"Please enter this confirmation code on the password reset page to proceed with the process.\r\n"
				+ "\r\n"
				+ "If you did not request a password reset, please disregard this email. Your account will not be affected.\r\n"
				+ "\r\n"
				+ "Thank you for your attention.\r\n"
				+ "\r\n"
				+ "Sincerely,\r\n"
				+ " ADELA"
				)
				
				;
		
		send(mailInfoDto);
	}

}
