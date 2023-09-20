package com.web.SellShoes.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.web.SellShoes.entity.User;
import com.web.SellShoes.repository.UserRepository;
import com.web.SellShoes.service.UserService;


@Service
public class UserServiceImpl implements UserService, UserDetailsService{
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<User> findUserByEmail(String email) {
		Optional<User> user = userRepository.findUserByEmail(email);
		return user;
	}

	@Override
	public Optional<User> findUserByPhone(String phone) {
		Optional<User> user = userRepository.findUserByPhone(phone);
		return user;
	}

	@Override
	public void save(User user) {
		userRepository.save(user);
		
	}

	@Override
	public User getUser(Integer userId) {
		// TODO Auto-generated method stub
		Optional<User> user = userRepository.findById(userId);
		if (user.isEmpty()) {
			
		}
		return user.get();
	}

}
