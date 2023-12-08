package com.web.SellShoes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.SellShoes.entity.Account;
import com.web.SellShoes.entity.Category;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>{
	public Optional<Account> findAccountByEmail(String email);
	public Optional<Account> findAccountByPhone(String phone);
	//@Query("SELECT a FROM Account a WHERE a.id, a.fullName, a.address, a.email, a.phone, a.createAt, a.updateAt, a.role_id and a.active")
	List<Account> findAll();
	
	@Query("SELECT c FROM Account c " )
	public Page<Account> findAccountPage(PageRequest accountPageable);
	
	@Query("SELECT c FROM Account c WHERE  c.fullName LIKE %:keyword% or c.phone LIKE %:keyword% or c.address LIKE %:keyword%" )
	public Page<Account> findByKeyWord(PageRequest accountPageable, String keyword);
}
