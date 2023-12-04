package com.web.SellShoes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.web.SellShoes.entity.Account;

public interface AccountService {
	public Optional<Account> findUserByEmail(String email);
	public Optional<Account> findUserByPhone(String phone );
	public void save(Account user);
	public Account getAccount(Integer userId);
	List<Account> getAll();
	List<Account> findAll();
	public Page<Account> getAllAccount(int page, int size);
	public Page<Account> getAccountyByKey(int page, int size, String keyword);
	
}
