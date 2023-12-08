package com.web.SellShoes.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.SellShoes.dto.requestDto.AccountRequestRole;
import com.web.SellShoes.dto.responseDto.AccountPageResponseDto;
import com.web.SellShoes.dto.responseDto.AccountResponseDto;
import com.web.SellShoes.entity.Account;
import com.web.SellShoes.entity.Role;
import com.web.SellShoes.service.AccountService;
import com.web.SellShoes.service.RoleService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/account")
public class AccountController {
	private final AccountService accountService;
    private final RoleService roleService;
	@GetMapping()
	public String list(HttpSession session, Model model) {
		model.addAttribute("fullName",(String) session.getAttribute("fullName"));
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
	public ResponseEntity<AccountPageResponseDto> getAccountPage(@RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = "0") int page, @RequestParam(required = false) String keyword) {
		//keyword = "";
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
	@PostMapping(value="/updateRole")
	public ResponseEntity<?> updateRole(@RequestBody AccountRequestRole accountRequestRole) {
	    try {
	        Integer accountId = accountRequestRole.getId(); // Lấy ID của tài khoản cần cập nhật 
	        String newRoleName = accountRequestRole.getRoleName(); // Lấy tên vai trò mới
	        Optional<Account> optionalAccount = Optional.ofNullable(accountService.getAccount(accountId));
	        if (optionalAccount.isPresent()) {
	            Account accountToUpdateRoleName = optionalAccount.get();
	            
	            // Lấy vai trò mới từ RoleService
	            Optional<Role> roles = roleService.getRoleByName(newRoleName);
	            if (roles.isPresent()) {
	                Role newRole = roles.get();	                
	                // Cập nhật vai trò mới cho tài khoản
	                accountToUpdateRoleName.setRole(newRole);
	                accountToUpdateRoleName.setUpdateAt(LocalDate.now());
	                accountService.save(accountToUpdateRoleName);
	                return ResponseEntity.ok("Successfully updated roles for the account");
	            } else {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy vai trò có tên: " + newRoleName);
	            }
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy tài khoản có ID: " + accountId);
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi cập nhật vai trò: " + e.getMessage());
	    }
	}



}
