package com.web.SellShoes.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.SellShoes.dto.requestDto.UserRequestDto;
import com.web.SellShoes.entity.ConfirmationToken;
import com.web.SellShoes.entity.Role;
import com.web.SellShoes.entity.Account;
import com.web.SellShoes.mapper.Mapper;
import com.web.SellShoes.service.ConfirmationTokenService;
import com.web.SellShoes.service.MailerService;
import com.web.SellShoes.service.RoleService;
import com.web.SellShoes.service.AccountService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/register")
public class RegisterController {

	private final AccountService userService;
	private final RoleService roleService;
	private static final String USER = "USER";
	private final PasswordEncoder passwordEncoder;
	private final Mapper mapper;
	private final MailerService mailerService;
	private final ConfirmationTokenService confirmationTokenService;

	@GetMapping()
	public String displayRegistration(Model model) {
		model.addAttribute("userRequestDto", new UserRequestDto());
		return "register/register";
	}

	@PostMapping(value = "/checkRegister")
	public String register(Model model, @ModelAttribute("userRequestDto") @Valid UserRequestDto userRequestDto,
			BindingResult bindingResult) {

		Optional<Account> userByEmail = userService.findUserByEmail(userRequestDto.getEmail());
		Optional<Account> userByPhone = userService.findUserByPhone(userRequestDto.getPhone());
		Optional<Role> role = roleService.getRoleByName(USER);

		if (bindingResult.hasErrors() || userByEmail.isPresent() || userByPhone.isPresent()
				|| !userRequestDto.getPassword().equals(userRequestDto.getConfirmPassword())) {

			if (userByEmail.isPresent()) {
				model.addAttribute("emailDuplicate", "Email already exists! Please enter a new one");
			}
			if (userByPhone.isPresent()) {
				model.addAttribute("phoneDuplicate", "Phone already exists! Please enter a new one");
			}
			if (!userRequestDto.getPassword().equals(userRequestDto.getConfirmPassword())) {
				model.addAttribute("notMatchPass", "Passwords do not match");
			}
			return "register/register";
		}

		String password = passwordEncoder.encode(userRequestDto.getPassword());
		userRequestDto.setPassword(password);
		Account user = mapper.userRquestDtoMapToUser(userRequestDto);
		user.setRole(role.get());
		userService.save(user);
		mailerService.sendEmailToConfirmAccount(user);

		return "register/success";
	}

	@RequestMapping(value = "/confirm-account", method = { RequestMethod.GET, RequestMethod.POST })
	public String ConfirmAccount(Model model, @RequestParam("token") String confirmationToken) {

		Optional<ConfirmationToken> token = confirmationTokenService.getConfirmationTokenByToken(confirmationToken);
		if (!token.isPresent()) {
			model.addAttribute("message", "The link is invalid or broken!");
			return "register/verifyTokenError";
		} else {
			if (token.get().getExpiryDate().isBefore(LocalDateTime.now())) {
				model.addAttribute("message", "The link is invalid or broken!");
				return "register/verifyTokenError";
			}
			Optional<Account> user = userService.findUserByEmail(token.get().getAccount().getEmail());
			user.get().setActive(true);
			userService.save(user.get());
			model.addAttribute("fullname", user.get().getFullName());
			return "register/registration-confirmed";
		}
	}
	
	@PostMapping(value = "/resendMail")
	public String sendMailVerifyAccount(@RequestParam("email") String email, Model model) {
		Optional<Account> user = userService.findUserByEmail(email);

		mailerService.sendEmailToConfirmAccount(user.get());
		model.addAttribute("email", user.get().getEmail());

		return "register/success";
	}

}
