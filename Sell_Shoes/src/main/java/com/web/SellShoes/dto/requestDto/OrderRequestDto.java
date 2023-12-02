package com.web.SellShoes.dto.requestDto;


import java.util.List;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import com.web.SellShoes.entity.Promotion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderRequestDto {	
	@Size(min = 1, max = 70,message="Full Name cannot be empty and exceed 70 characters")
	private String fullName;
	
	@Pattern(regexp = "^(\\d{10}|\\d{12})$", message = "Phone can not empty and should be between 10 to 12 digits")
	private String phone_Number;
	
	@Size(min=1, max = 100,message="Address must not exceed 100 characters")
	private String address;	
	
	@Size(min=0, max = 500,message="Note must not exceed 500 characters")
	private String note;
	
	private Float confirmedPrice;
	
	private Integer promotionId;
 
	@Size(min=1, max = 100,message="Address must not exceed 100 characters")
	private String province;	
	@Size(min=1, max = 100,message="Address must not exceed 100 characters")
	private String district;	
	@Size(min=1, max = 100,message="Address must not exceed 100 characters")
	private String ward;	
	
	private Promotion promotion;
	List<Integer>cartIds;
}
