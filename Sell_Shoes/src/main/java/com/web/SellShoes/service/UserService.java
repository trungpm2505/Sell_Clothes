package com.web.SellShoes.service;

import java.util.Optional;

import com.web.SellShoes.entity.User;

public interface UserService {
	public Optional<User> findUserByEmail(String email);
	public Optional<User> findUserByPhone(String phone );
	public void save(User user);
	public User getUser(Integer userId);
}
