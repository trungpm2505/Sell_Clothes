package com.web.SellShoes.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ConfirmationToken {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;

	    private String token;
	    
	    @OneToOne(targetEntity = Account.class, fetch = FetchType.EAGER)
	    @JoinColumn(nullable = false, name = "account_id")
	    private Account account;

	    private LocalDateTime expiryDate;

		public ConfirmationToken(String token, Account account, LocalDateTime expiryDate) {
			super();
			this.token = token;
			this.account = account;
			this.expiryDate = expiryDate;
		}
}
