package com.web.SellShoes.serviceImpl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.web.SellShoes.entity.Account;
import com.web.SellShoes.entity.ConfirmationToken;
import com.web.SellShoes.repository.ConfirmationTokenRepository;
import com.web.SellShoes.service.ConfirmationTokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {
	private final ConfirmationTokenRepository confirmationTokenRepository;
	
	@Override
	public ConfirmationToken save(ConfirmationToken ConfirmationToken) {
		confirmationTokenRepository.save(ConfirmationToken);
        return ConfirmationToken;
	}

	@Override
	public Optional<ConfirmationToken> getConfirmationTokenByToken(String token) {
		return confirmationTokenRepository.findByToken(token);
	}

	@Override
	public Optional<ConfirmationToken> getConfirmationTokenByTokenAndAccount(String token, Account account) {
		return confirmationTokenRepository.findByTokenAndAccount(token,account);
	}

}
