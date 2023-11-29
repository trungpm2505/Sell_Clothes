package com.web.SellShoes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.SellShoes.entity.Account;
import com.web.SellShoes.entity.ConfirmationToken;
@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Integer>{
	Optional<ConfirmationToken> findByToken(String token);
	Optional<ConfirmationToken> findByTokenAndAccount(String token,Account account);
}
