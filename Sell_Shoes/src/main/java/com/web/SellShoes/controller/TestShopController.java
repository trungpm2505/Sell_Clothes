package com.web.SellShoes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/shop")
public class TestShopController {
	@GetMapping("index")
	public String index() {
		// test từng trang thay đường dẫn
		return "shop/shopcontent/trangchu";
	}
	
	@GetMapping("product")
	public String product() {
		// test từng trang thay đường dẫn
		return "shop/shopcontent/product";
	}
	@GetMapping("shop")
	public String shop() {
		// test từng trang thay đường dẫn
		return "shop/shopcontent/shop1";
	}
	@GetMapping("contact")
	public String contact() {
		// test từng trang thay đường dẫn
		return "shop/shopcontent/contact";
	}
	@GetMapping("account")
	public String account() {
		// test từng trang thay đường dẫn
		return "shop/shopcontent/my-account1";
	}
	@GetMapping("cart")
	public String cart() {
		// test từng trang thay đường dẫn
		return "shop/shopcontent/cart";
	}
	@GetMapping("checkout")
	public String checkout() {
		// test từng trang thay đường dẫn
		return "shop/shopcontent/checkout";
	}

}
