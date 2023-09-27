package com.web.SellShoes.service;

import java.util.Optional;

import com.web.SellShoes.entity.Account;

public interface AccountService {
	public Optional<Account> findUserByEmail(String email);
	public Optional<Account> findUserByPhone(String phone );
	public void save(Account user);
	public Account getAccount(Integer userId);
}
