package com.web.SellClothes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/login")
public class LoginController {
	
	@ResponseBody
	@GetMapping
	public String login() {
		return"Đây là trang login!";
	}
}
