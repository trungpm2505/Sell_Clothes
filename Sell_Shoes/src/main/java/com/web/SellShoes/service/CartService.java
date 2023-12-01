package com.web.SellShoes.service;

import java.util.List;
import java.util.Optional;

import com.web.SellShoes.entity.Account;
import com.web.SellShoes.entity.Cart;
import com.web.SellShoes.entity.Product;
import com.web.SellShoes.entity.Variant;

public interface CartService {
public void save(Cart cart);	
public Optional<Cart>findCartByVariantAndAccount(Variant variant,Account account);
public List<Cart>findCartByAccount(Account account);
public Optional<Cart>getCartById(Integer id);
public void deleteCart(Integer id);
public void deleteAllCart();
}
