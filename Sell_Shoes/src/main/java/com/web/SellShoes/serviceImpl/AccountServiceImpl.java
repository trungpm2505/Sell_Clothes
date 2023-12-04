package com.web.SellShoes.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import com.web.SellShoes.entity.Account;
import com.web.SellShoes.jwt.CustomUserDetails;
import com.web.SellShoes.repository.AccountRepository;
import com.web.SellShoes.service.AccountService;


@Service
public class AccountServiceImpl implements AccountService, UserDetailsService{
	@Autowired
	private AccountRepository accountRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Account> account = accountRepository.findAccountByEmail(username);
		CustomUserDetails customUserDetails = new CustomUserDetails(account.get());
		return customUserDetails;
	}
    
	@Override
	public List<Account> getAll(){
		List<Account> accounts = accountRepository.findAll();
		return accounts;
	}
	
	@Override
	public List<Account> findAll(){
		return accountRepository.findAll();
	}
	@Override
	public Optional<Account> findUserByEmail(String email) {
		Optional<Account> user = accountRepository.findAccountByEmail(email);
		return user;
	}

	@Override
	public Optional<Account> findUserByPhone(String phone) {
		Optional<Account> user = accountRepository.findAccountByPhone(phone);
		return user;
	}

	@Override
	public void save(Account user) {
		accountRepository.save(user);
		
	}

	@Override
	public Account getAccount(Integer userId) {
		// TODO Auto-generated method stub
		Optional<Account> user = accountRepository.findById(userId);
		if (user.isEmpty()) {
			
		}
		return user.get();
	}

	@Override
	public Page<Account> getAllAccount(int pagenumber, int size) {
		// TODO Auto-generated method stub
		PageRequest accountPageable = PageRequest.of(pagenumber, size, Sort.by(Sort.Direction.ASC, "fullName"));

	    Page<Account> accountPage = accountRepository.findAccountPage(accountPageable);

	    return accountPage;
	}

	@Override
	public Page<Account> getAccountyByKey(int pagenumber, int size, String keyword) {
		PageRequest accountPageable = PageRequest.of(pagenumber, size, Sort.by(Sort.Direction.ASC, "fullName"));

	    return accountRepository.findByKeyWord(accountPageable, keyword);
	}

}
