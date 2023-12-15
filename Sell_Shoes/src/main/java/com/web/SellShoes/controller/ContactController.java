package com.web.SellShoes.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/contact")
public class ContactController {
	
	@GetMapping()
	public String viewContact(Model model, HttpSession session) {
		return "/shop/shopcontent/contact";
	}
}
