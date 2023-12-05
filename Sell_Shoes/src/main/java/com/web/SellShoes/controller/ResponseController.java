package com.web.SellShoes.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.SellShoes.dto.requestDto.ResponseRequestDto;
import com.web.SellShoes.entity.Account;
import com.web.SellShoes.entity.ResponseRate;
import com.web.SellShoes.service.AccountService;
import com.web.SellShoes.service.RateService;
import com.web.SellShoes.service.ResponseService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/response")
public class ResponseController {
	private final ResponseService responseService;
	private final RateService rateService;
	private final AccountService accountService;
	
	
	@PostMapping(value = "/add")
	@Transactional
	@ResponseBody
	public ResponseEntity<?> addResponse(HttpSession session,@Valid @RequestBody ResponseRequestDto responseRequestDto) {
		
		String email = (String) session.getAttribute("email");
	    Optional<Account> accountByEmail = accountService.findUserByEmail(email);
	    if(accountByEmail.isEmpty()) {
	    	return ResponseEntity.badRequest().body("Vui lòng đăng nhập để bình luận!!");
	    
	    }
	    if (responseRequestDto.getContent() == null || responseRequestDto.getContent().isEmpty()) {
	    	return ResponseEntity.badRequest().body("Vui lòng nhập content!!");
		}
		ResponseRate responseRate = new ResponseRate();
		responseRate.setRate(rateService.getRateById(responseRequestDto.getRateId()));
		responseRate.setContent(responseRequestDto.getContent());
		responseRate.setAccount(accountByEmail.get());
		responseService.save(responseRate);
			
		return ResponseEntity.ok().body("{\"message\": \"Response rate successfully\"}");
	}
}
