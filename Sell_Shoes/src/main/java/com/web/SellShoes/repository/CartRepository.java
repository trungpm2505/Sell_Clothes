package com.web.SellShoes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.web.SellShoes.entity.Account;
import com.web.SellShoes.entity.Cart;
import com.web.SellShoes.entity.Product;
import com.web.SellShoes.entity.Variant;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
public Optional<Cart>findCartByVariantAndAccount(Variant variant, Account account);
public List<Cart>findCartByAccount(Account account);

}
