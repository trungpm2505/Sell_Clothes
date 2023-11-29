package com.web.SellShoes.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ResponseRate {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	@Column(nullable = false)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	LocalDateTime createAt = LocalDateTime.now();
	
	 @Column(nullable = false, length = 1500, columnDefinition = "NVARCHAR(1500)")
	 private String content;
	
	@ManyToOne
    @JoinColumn(name = "rate_id",nullable = false, referencedColumnName = "id")
    private Rate rate;
    
	@ManyToOne
    @JoinColumn(name = "account_id",nullable = false, referencedColumnName = "id")
    private Account account;
}
