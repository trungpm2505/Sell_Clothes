package com.example.demo2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class demo2 {
	
	@ResponseBody
	@GetMapping
	public String demo() {
		return "Đây là dự án tốt nghiệp 1";
	}
}
