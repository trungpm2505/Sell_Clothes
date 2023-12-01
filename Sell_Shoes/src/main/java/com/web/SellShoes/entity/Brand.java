package com.web.SellShoes.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Brand {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, length = 50, columnDefinition = "NVARCHAR(50)")
	private String name;
	
	@Column(nullable = false, length = 200)
	private String thumbnail;

	@Column(nullable = true, length = 200, columnDefinition = "NVARCHAR(200)")
	private String description;
	
	@Column(nullable = false)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate createAt = LocalDate.now();

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate updateAt;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate deleteAt;
}
