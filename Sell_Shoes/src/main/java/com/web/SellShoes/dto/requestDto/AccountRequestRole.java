package com.web.SellShoes.dto.requestDto;

import com.web.SellShoes.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class AccountRequestRole {
	private Integer id;
	private String roleName;
}
