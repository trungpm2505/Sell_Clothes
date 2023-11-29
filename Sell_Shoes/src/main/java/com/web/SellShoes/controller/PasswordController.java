package com.web.SellShoes.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.SellShoes.service.AccountService;
import com.web.SellShoes.service.ConfirmationTokenService;
import com.web.SellShoes.service.MailerService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value = "/password")
@RequiredArgsConstructor
public class PasswordController {
	private final AccountService accountService;
	private final MailerService mailerService;
	private final PasswordEncoder passwordEncoder;
	private final ConfirmationTokenService confirmationTokenService;
	
	@GetMapping("/reset")
	public String changePassWord(Model model) {
		return "/password/resetPassword";
	}
	
}
