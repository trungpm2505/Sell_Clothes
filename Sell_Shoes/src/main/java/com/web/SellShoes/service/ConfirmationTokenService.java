package com.web.SellShoes.service;

import java.util.Optional;

import com.web.SellShoes.entity.Account;
import com.web.SellShoes.entity.ConfirmationToken;

public interface ConfirmationTokenService {
	public ConfirmationToken save(ConfirmationToken ConfirmationToken);
	public Optional<ConfirmationToken> getConfirmationTokenByToken(String token);
	public Optional<ConfirmationToken> getConfirmationTokenByTokenAndAccount(String token,Account account);
}
