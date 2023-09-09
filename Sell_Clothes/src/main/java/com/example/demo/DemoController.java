package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller

public class DemoController {
	
	@ResponseBody
	@GetMapping
	public String demo() {
		return "Đây là dự án tốt nghiệp";
	}
}
