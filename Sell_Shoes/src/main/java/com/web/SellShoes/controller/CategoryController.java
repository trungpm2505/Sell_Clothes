package com.web.SellShoes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class CategoryController {
	@GetMapping("add")
	public String list(ModelMap m) {

		return "admin/categories/add";
	}
}
