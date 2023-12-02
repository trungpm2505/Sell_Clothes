package com.web.SellShoes.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Promotion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate createAt = LocalDate.now();

	@Column(name = "name", nullable = false, length = 300, columnDefinition = "NVARCHAR(300)")
	private String name;

	@Column(name = "coupon_code", unique = true)
	private String couponCode;

	@Column(name = "discount_type")
	private int discountType;

	@Column(name = "discount_value")
	private long discountValue;

	@Column(name = "maximum_discount_value")
	private long maximumDiscountValue;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate expiredAt;

	@Column(name = "is_active")
	private boolean isActive;

	@Column(name = "is_public")
	private boolean isPublic;

	@Column(nullable = true)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate deleteAt;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate updateAt;

	@OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL)
	private List<Order> orders = new ArrayList<>();
}
