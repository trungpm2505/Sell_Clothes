package com.web.SellShoes.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.SellShoes.dto.requestDto.LoginRequestDto;
import com.web.SellShoes.dto.responseDto.AuthenticationResponseDto;
import com.web.SellShoes.entity.Account;
import com.web.SellShoes.jwt.CustomUserDetails;
import com.web.SellShoes.jwt.JwtTokenProvider;
import com.web.SellShoes.repository.AccountRepository;
import com.web.SellShoes.service.AccountService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/login")
public class LoginController {
	private final AccountRepository accountRepository;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider tokenProvider;

	@GetMapping()
	public String authenticateUser(Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			model.addAttribute("loginRequestDto", new LoginRequestDto());
			return "/login/login";
		}
		Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
		if (roles.contains("ADMIN")) {
			return "redirect:/product/admin";
		} else {
			return "redirect:/product/all-product";
		}

	}

	@PostMapping(value = "/checkLogin")
	@ResponseBody
	public ResponseEntity<?> authenticateUser(HttpSession session, @Valid @RequestBody LoginRequestDto loginRequestDto,
			BindingResult bindingResult, Model model) {

		Map<String, Object> errors = new HashMap<>();
		if (bindingResult.hasErrors()) {
			errors.put("bindingErrors", bindingResult.getAllErrors());
			return ResponseEntity.badRequest().body(errors);
		}
		AuthenticationResponseDto authenticationResponseDto = new AuthenticationResponseDto();

		Optional<Account> account = accountRepository.findAccountByEmail(loginRequestDto.getEmail());
		if (!account.isPresent()) {
			errors.put("EmailErrors", "The account with email is not exist");
			return ResponseEntity.badRequest().body(errors);
		}

		if (account.isPresent() && !account.get().isActive()) {
			errors.put("accountError",
					"The account has not been verified, please check your email to verify your account before logging in");
			authenticationResponseDto.setMessage(
					"The account has not been verified, please check your email to verify your account before logging in");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authenticationResponseDto);
		}

		Authentication authentication = null;
		try {
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));
		} catch (BadCredentialsException ex) {
			errors.put("PasswordErrors", "The password is incorrect, please check again");
			return ResponseEntity.badRequest().body(errors);
		}

		// cho phép Spring Security biết rằng người dùng đã được xác thực.
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
		// get role
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		String role = null;
		for (GrantedAuthority authority : authorities) {
			if (authority.getAuthority().equals("ADMIN")) {
				role = "ADMIN";
				break;
			} else if (authority.getAuthority().equals("USER")) {
				role = "USER";
				break;
			}
		}	
		
		session.setAttribute("fullName",account.get().getFullName());
		session.setAttribute("role",role);
		session.setAttribute("email",account.get().getEmail());
		model.addAttribute("fullName", (String) session.getAttribute("fullName"));
		model.addAttribute("role", (String) session.getAttribute("role"));
		authenticationResponseDto.setToken(token);
		authenticationResponseDto.setRole(role);
		
		return ResponseEntity.ok(authenticationResponseDto);
	}
}
