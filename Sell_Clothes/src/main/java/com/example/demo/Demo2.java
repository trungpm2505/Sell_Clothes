package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller

public class Demo2 {
	@ResponseBody
	@GetMapping
	public String demo() {
		return "hihi";
	}
}
