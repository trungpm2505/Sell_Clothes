package com.web.SellShoes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.SellShoes.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>{
	public Optional<Account> findAccountByEmail(String email);
	public Optional<Account> findAccountByPhone(String phone);
}
