package com.web.SellShoes.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.SellShoes.entity.Account;
import com.web.SellShoes.entity.Cart;
import com.web.SellShoes.entity.Product;
import com.web.SellShoes.entity.Variant;
import com.web.SellShoes.repository.CartRepository;
import com.web.SellShoes.service.CartService;

@Service
public class CartServiceImpl implements CartService{
@Autowired
CartRepository cartRepository;
	@Override
	public Optional<Cart> findCartByVariantAndAccount(Variant variant,Account account) {
		return cartRepository.findCartByVariantAndAccount(variant, account);
	}

	@Override
	public List<Cart> findCartByAccount(Account account) {
		List<Cart>list=cartRepository.findCartByAccount(account);
		return list;
	}

	@Override
	public Optional<Cart> getCartById(Integer id) {
		return cartRepository.findById(id);
	}

	@Override
	public void deleteCart(Integer id) {
		cartRepository.deleteById(id);
		
	}



	@Override
	public void save(Cart cart) {
		cartRepository.save(cart);
		
	}

	@Override
	public void deleteAllCart() {
		cartRepository.deleteAll();
		
	}

}
