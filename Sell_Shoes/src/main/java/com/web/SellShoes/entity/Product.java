package com.web.SellShoes.entity;

import java.time.LocalDate;

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
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, length = 200)
	private String title;
	
	@Column(nullable = false, length = 1500)
	private String discription;
	
	@Column(nullable = false)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate createAt = LocalDate.now();

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate updateAt;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate deleteAt;

	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false, referencedColumnName = "id")
	private Category category;
	
	@ManyToOne
	@JoinColumn(name = "brand_id", nullable = false, referencedColumnName = "id")
	private Brand brand;
}
