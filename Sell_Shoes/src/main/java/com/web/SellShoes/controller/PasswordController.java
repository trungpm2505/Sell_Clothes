package com.web.SellShoes.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.SellShoes.dto.requestDto.CheckTokenResetPass;
import com.web.SellShoes.dto.requestDto.EmailRequestDto;
import com.web.SellShoes.dto.requestDto.ResetPassRequestDto;
import com.web.SellShoes.entity.Account;
import com.web.SellShoes.entity.ConfirmationToken;
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

	@PostMapping("/sentEmail")
	public ResponseEntity<String> sentEmailToResetPassword(@Valid @RequestBody EmailRequestDto email,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {

			return ResponseEntity.badRequest().body("{\"message\": \"Please enter valid email.\"}");

		}
		Optional<Account> accountByEmail = accountService.findUserByEmail(email.getEmail());
		if (accountByEmail.isEmpty()) {

			return ResponseEntity.badRequest().body("{\"message\": \"No account exists with the email you entered.\"}");
		}

		if (!accountByEmail.get().isActive()) {
			return ResponseEntity.badRequest()
					.body("{\"message\": \"The account has not been verified, please check your email to verify.\"}");
		}

		mailerService.sendEmailToResetPassword(accountByEmail.get());

		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("{\"message\": \"Ok\"}");
	}

	@GetMapping(value = "/getTokenReset")
	public String getTokenReset() {
		return "/password/getTokenResetPass";
	}

	@PostMapping("/checkTokenReset")
	@ResponseBody
	public ResponseEntity<String> checkTokenReset(@Valid @RequestBody CheckTokenResetPass checkTokenResetPass,
			BindingResult bindingResult) {

		System.out.println("email: " + checkTokenResetPass.getEmail());
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body("{\"message\": \"Code has 6 digits.\"}");
		}

		Optional<Account> account = accountService.findUserByEmail(checkTokenResetPass.getEmail());
		Optional<ConfirmationToken> token = confirmationTokenService
				.getConfirmationTokenByTokenAndAccount(checkTokenResetPass.getToken(), account.get());

		// if token is not valid
		if (!token.isPresent()) {
			return ResponseEntity.badRequest().body("{\"message\": \"The link is invalid!.\"}");
		}
		// if token is valid but expiryDate

		if (token.isPresent()) {

			if (token.get().getExpiryDate().isBefore(LocalDateTime.now())) {
				return ResponseEntity.badRequest()
						.body("{\"message\": \"The link is broken.Click resent to get code again.!\"}");
			}

			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("{\"message\": \"Ok\"}");
		} else {
			return ResponseEntity.badRequest().body("{\"message\": \"The link is invalid!\"}");
		}

	}

	@GetMapping(value = "/setNewPassView")
	public String setNewPassView() {
		return "password/resetNewPassword";
	}

	@PutMapping("/checkResetPass")
	@ResponseBody
	public ResponseEntity<?> setNewPass(@Valid @ModelAttribute ResetPassRequestDto resetPassRequestDto,
			BindingResult bindingResult) {
		Map<String, Object> errors = new HashMap<>();
		if (bindingResult.hasErrors()) {
			// Xử lý lỗi binding và tạo danh sách thông báo lỗi
			errors.put("bindingErrors", bindingResult.getAllErrors());
		}

		if (!bindingResult.getFieldErrors().stream().anyMatch(error -> error.getField().equals("newPass"))) {
			if (!resetPassRequestDto.getNewPass().equals(resetPassRequestDto.getNewPassAgain())) {
				errors.put("message", "New password not match.");
			}
		}

		if (!errors.isEmpty()) {
			return ResponseEntity.badRequest().body(errors);
		}
		String encodedPassword = passwordEncoder.encode(resetPassRequestDto.getNewPass());
		Optional<Account> accountByEmail = accountService.findUserByEmail(resetPassRequestDto.getEmail());
		accountByEmail.get().setPassword(encodedPassword);

		accountService.save(accountByEmail.get());

		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("{\"message\": \"Reset password successfully\"}");
	}
}
