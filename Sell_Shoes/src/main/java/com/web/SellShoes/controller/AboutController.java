package com.web.SellShoes.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/about")
public class AboutController {
	@GetMapping("/aboutMe")
	public String aboutMe(Model model, HttpSession session) {
		model.addAttribute("fullName", (String) session.getAttribute("fullName"));
		return "shop/shopcontent/about";
	}
}
