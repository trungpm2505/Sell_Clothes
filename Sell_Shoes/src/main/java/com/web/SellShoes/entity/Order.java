package com.web.SellShoes.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, length = 60)
	private String fullName;
	
	@Column(nullable = false, length = 11)
	private String phone_Number;
	
	@Column(nullable = false, length = 100)
	private String adrress;
	
	@Column(nullable = true, length = 50)
	private String note;
	
	@Column(nullable = false)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate order_date = LocalDate.now();
	
	@Column(nullable = false)
	private int status = 1;
	
	@Column(nullable = false)
	private float totalMoney;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id", nullable = true, referencedColumnName = "id")
	private Account account;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<OrderDetail> orderDetailsSet = new ArrayList<>();
	
	@ManyToOne
	@JoinColumn(name = "promotion_id", nullable = true, referencedColumnName = "id")
	private Promotion promotion;
}
