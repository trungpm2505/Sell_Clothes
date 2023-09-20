package com.web.SellShoes.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.SellShoes.entity.Role;
import com.web.SellShoes.repository.RoleRepository;
import com.web.SellShoes.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService{
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Optional<Role> getRoleByName(String name) {
		Optional<Role> role = roleRepository.findRoleByRoleName(name);
		return role;
	}

}
