package com.web.SellShoes.dto.requestDto;

import java.time.LocalDate;

import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PromotionRequestDto {
	private Integer id;

	@NotBlank(message = "CouponCode cannot be empty!")
    @Pattern(regexp = "^[0-9A-Z-]+$", message = "CouponCode is not in the correct format!")
    private String couponCode;

    @NotBlank(message = "Name cannot be empty!")
    @Size(min = 1, max = 300, message = "Name cannot be empty and must not exceed 300 characters!")
    private String name;

    @Min(value = 1, message = "DiscountType must be at least 1")
    @Max(value = 2, message = "DiscountType must be at most 2")
    private int discountType;

    @NotNull(message = "DiscountValue cannot be null")
    private Long discountValue;

    
    private Long maxValue;

    private boolean isPublic;
    private boolean active;

    @Future(message = "ExpiredDate must be in the future")
    @NotNull(message = "ExpiredDate cannot be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate expiredDate;


}
