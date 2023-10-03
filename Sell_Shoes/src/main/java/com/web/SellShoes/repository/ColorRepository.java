package com.web.SellShoes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.SellShoes.entity.Color;

@Repository
public interface ColorRepository extends JpaRepository<Color, Integer>{

}
