package com.web.SellShoes.service;

import java.util.Optional;

import com.web.SellShoes.entity.Role;

public interface RoleService {
	public Optional<Role> getRoleByName(String name);
}
