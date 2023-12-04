package com.web.SellShoes.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.SellShoes.dto.responseDto.AccountPageResponseDto;
import com.web.SellShoes.dto.responseDto.AccountResponseDto;
import com.web.SellShoes.entity.Account;
import com.web.SellShoes.service.AccountService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/account")
public class AccountController {
	private final AccountService accountService;

	@GetMapping()
	public String list(Model model) {
		return "admin/account/list";
	}

	@GetMapping(value = "/getAll")
	public ResponseEntity<?> getAllAccount(Model model) {
		List<Account> accounts = accountService.getAll();
		List<AccountResponseDto> accountResponseDtos = accounts.stream()
				.map(account -> new AccountResponseDto(account.getId(), account.getFullName(), account.getEmail(),
						account.getPhone(), account.getAddress(), account.getRole().getRoleName(),
						account.getCreateAt(), account.getUpdateAt(), account.isActive()))
				.collect(Collectors.toList());
		return ResponseEntity.ok(accountResponseDtos);
	}

	@GetMapping(value = "/getAccountPage")
	public ResponseEntity<AccountPageResponseDto> getAccountPage(@RequestParam(defaultValue = "8") int size,
			@RequestParam(defaultValue = "0") int page, @RequestParam(required = false) String keyword) {
		keyword = "";
		Page<Account> accountPage = null;
		if (keyword == null || keyword.isEmpty()) {
			accountPage = accountService.getAllAccount(page, size);
		} else {
			accountPage = accountService.getAccountyByKey(page, size, keyword);
		}

		List<AccountResponseDto> accountResponseDtos = accountPage.stream()
				.map(account -> new AccountResponseDto(account.getId(), account.getFullName(), account.getEmail(),
						account.getPhone(), account.getAddress(), account.getRole().getRoleName(),
						account.getCreateAt(), account.getUpdateAt(), account.isActive()))
				.collect(Collectors.toList());
		AccountPageResponseDto accountPageResponseDto = new AccountPageResponseDto(accountPage.getTotalPages(),
				accountPage.getNumber(), accountPage.getSize(), accountResponseDtos);
		return ResponseEntity.ok(accountPageResponseDto);
	}
}
