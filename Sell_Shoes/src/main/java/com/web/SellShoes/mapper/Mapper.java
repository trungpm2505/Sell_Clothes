package com.web.SellShoes.mapper;


import org.springframework.stereotype.Component;

import com.web.SellShoes.dto.requestDto.UserRequestDto;
import com.web.SellShoes.entity.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Mapper {
	public User userRquestDtoMapToUser(UserRequestDto userRequestDto) {
		User user = new User();
		user.setPassword(userRequestDto.getPassword());
		user.setFullName(userRequestDto.getFullname());
		user.setAddress(userRequestDto.getAddress());
		user.setPhone(userRequestDto.getPhone());
		user.setEmail(userRequestDto.getEmail());
		return user;
	}
}
