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
import javax.validation.constraints.Email;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class UserFeedback {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
    @Column(nullable = false, length = 60)
    private String fullName;
    
    @Email
    @Column(nullable = false,length = 60)
    private String email;
    
    @Column(nullable = false, length = 15)
    private String phone;
    
    @Column(nullable = false, length = 20)
    private String subjectName;
    
    @Column(nullable = false, length = 150)
    private String note;
    
    @Column(nullable = false)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate createAt = LocalDate.now();
    
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id", nullable = true)
	private Account account;
}
