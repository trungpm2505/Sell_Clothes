package com.web.SellShoes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.SellShoes.service.AccountService;
import com.web.SellShoes.service.RoleService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value = "/about")
public class AboutController {
	 @GetMapping("/aboutMe")
	    public String aboutMe() {
	        return "shop/shopcontent/about";
	    }
}
