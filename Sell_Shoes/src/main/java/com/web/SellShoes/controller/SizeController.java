package com.web.SellShoes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SizeController {
	@GetMapping("view")
	
	public String view() {
		return"/view/index";
	}
}
