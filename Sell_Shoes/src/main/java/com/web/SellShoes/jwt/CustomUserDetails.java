package com.web.SellShoes.jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.web.SellShoes.entity.Role;
import com.web.SellShoes.entity.Account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class CustomUserDetails implements UserDetails{

	private final Account account;
//  Role roles = user.getRole();
//  GrantedAuthority authorities = new SimpleGrantedAuthority(roles.getRoleName());
//  return Collections.singleton(authorities);
//  
	@Transactional
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities = new ArrayList<>();
        Role role = account.getRole();
        authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
     
        return authorities;
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}

