package com.web.SellShoes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.SellShoes.entity.Size;

@Repository
public interface SizeRepository extends JpaRepository<Size, Integer>{

}
