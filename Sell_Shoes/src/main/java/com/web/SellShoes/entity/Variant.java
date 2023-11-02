package com.web.SellShoes.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
public class Variant {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	@Column(nullable = false)
	private Float price;
	
	@Column(nullable = true)
	private Float currentPrice;
	
	@Column(nullable = false)
	private int quantity;
	
	@Column(nullable = true, length = 1500)
	private String note;
	
	@Column(nullable = false)
	private int status = 1;
	
	@Column(nullable = false)
	private int buyCount = 0;
	
	@Column(nullable = false)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate createAt = LocalDate.now();

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate updateAt;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate deleteAt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "size_id", nullable = false, referencedColumnName = "id")
	private Size size;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "color_id", nullable = false, referencedColumnName = "id")
	private Color color;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false, referencedColumnName = "id")
	private Product product;
}
