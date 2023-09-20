package com.web.SellShoes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.SellShoes.entity.Role;
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{
	public Optional<Role> findRoleByRoleName(String name);
}
