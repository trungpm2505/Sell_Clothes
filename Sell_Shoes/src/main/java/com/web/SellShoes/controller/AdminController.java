package com.web.SellShoes.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.SellShoes.dto.requestDto.UserRequestDto;
import com.web.SellShoes.entity.Account;
import com.web.SellShoes.service.AccountService;
import com.web.SellShoes.service.RoleService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/adminprofile")
public class AdminController {
	
	private final AccountService accountService;

	@GetMapping(value = "/profile")
	public String showUserProfile(Model model, HttpSession session) {
		String email = (String) session.getAttribute("email");

		Optional<Account> user = accountService.findUserByEmail(email);

		if (user != null) {
			model.addAttribute("user", user);
			return "/admin/account/profile";
		} else {
			return "redirect:/login"; //
		}
	}
	
	

	
	@PutMapping("/editAccount")
	@ResponseBody
	public ResponseEntity<?> updateProfile(@Valid @ModelAttribute UserRequestDto userRequestDto,
	        BindingResult bindingResult, Principal principal) {
	    
	    Map<String, Object> errors = new HashMap<>();

	    if (bindingResult.hasErrors()) {
	        errors.put("bindingErrors", bindingResult.getAllErrors());
	        return ResponseEntity.badRequest().body(errors);
	    }
	    
	    Account accountById = accountService.getAccount(userRequestDto.getId());
	    Optional<Account> userByPhone = accountService.findUserByPhone(userRequestDto.getPhone());
	    
	    if (userByPhone.isPresent() && !userByPhone.get().getId().equals(accountById.getId())) {
	        errors.put("phone", "Số điện thoại đã tồn tại! Vui lòng nhập một số điện thoại mới.");
	        return ResponseEntity.badRequest().body(errors);
	    }
	    
	    //viết lưu dữ liệu từ đây nha
	 // Update user profile data
	    accountById.setFullName(userRequestDto.getFullname());
	    accountById.setAddress(userRequestDto.getAddress());
	    accountById.setPhone(userRequestDto.getPhone());
	    
	    // Save the updated user profile
	    accountService.save(accountById);
	    // Tiếp tục với phần còn lại của logic cập nhật
	    return ResponseEntity.ok().body("Cập nhật hồ sơ người dùng thành công");
	}
}
