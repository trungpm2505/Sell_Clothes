package com.web.SellShoes.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.web.SellShoes.dto.requestDto.RateRequestDto;
import com.web.SellShoes.dto.responseDto.RatePageResponseDto;
import com.web.SellShoes.dto.responseDto.RateResponseDto;
import com.web.SellShoes.entity.Account;
import com.web.SellShoes.entity.Rate;
import com.web.SellShoes.mapper.Mapper;
import com.web.SellShoes.service.AccountService;
import com.web.SellShoes.service.ImageService;
import com.web.SellShoes.service.OrderService;
import com.web.SellShoes.service.ProductService;
import com.web.SellShoes.service.RateService;
import com.web.SellShoes.service.ResponseService;
import com.web.SellShoes.service.VariantService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/rate")
public class RateController {
	private final RateService rateService;
	private final AccountService accountService;
	private final ImageService imageService;
	private final VariantService variantService;
	private final ResponseService responseService;
	private final OrderService orderService;
	private final ProductService productService;
	private final Mapper mapper;

	@PostMapping(value = "/add", consumes = "multipart/form-data", produces = { "application/json", "application/xml" })
	@Transactional
	@ResponseBody
	public ResponseEntity<?> upload(@RequestParam(value = "file", required = false) MultipartFile[] files,
			@Valid @ModelAttribute RateRequestDto rateRequestDto, HttpSession session) {
		String email = (String)session.getAttribute("email"); 
		Optional<Account> user = accountService.findUserByEmail(email);

		Rate rate = mapper.mapRateRequestDtoToRate(rateRequestDto, user.get());

		rateService.save(rate);

		if (rateRequestDto.getRateId() != null && imageService.getImageByRate(rate).size() != 0) {

			imageService.deleteByRate(rate);
		}
		// image
		if (files != null) {
			imageService.uploadFileForRate(files, rate);
		}

		return ResponseEntity.ok().body("{\"message\": \"Rating successfully\"}");
	}

	@GetMapping(value = "/getRatePage")
	@ResponseBody
	public ResponseEntity<RatePageResponseDto> getProductPage(@RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = "0") int page, @RequestParam Integer variantId,
			@RequestParam(required = false, defaultValue = "0") int rateScore) {
		Page<Rate> ratePage = null;
		if (rateScore == 0) {
			ratePage = rateService.finPage(page, size, variantService.getVariantById(variantId).get());
		} else {
			ratePage = rateService.finPageByRateScore(page, size, variantService.getVariantById(variantId).get(),
					rateScore);
		}

		List<RateResponseDto> rateResponseDtos = ratePage.stream()
				.map(rate -> new RateResponseDto(rate.getId(), rate.getCreateAt(), rate.getRating(), rate.getContent(),
						mapper.accountToAccountResponseDto(rate.getAccount()), responseService.getAll(rate),
						imageService.getImageByRate(rate)))
				.collect(Collectors.toList());

		RatePageResponseDto ratePageResponseDto = new RatePageResponseDto(ratePage.getTotalPages(),
				ratePage.getNumber(), ratePage.getSize(), rateResponseDtos);

		return ResponseEntity.ok(ratePageResponseDto);

	}

	@GetMapping(value = "/getRateProductPage")
	@ResponseBody
	public ResponseEntity<RatePageResponseDto> getProductPageProduct(@RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = "0") int page, @RequestParam Integer productId,
			@RequestParam(required = false, defaultValue = "0") int rateScore) {
		Page<Rate> ratePage = null;
		if (rateScore == 0) {
			ratePage = rateService.finPageProduct(page, size, productService.getProductById(productId).get());
		} else {
			ratePage = rateService.finPageByRateScoreProduct(page, size, productService.getProductById(productId).get(),
					rateScore);
		}

		List<RateResponseDto> rateResponseDtos = ratePage.stream()
				.map(rate -> new RateResponseDto(rate.getId(), rate.getCreateAt(), rate.getRating(), rate.getContent(),
						mapper.accountToAccountResponseDto(rate.getAccount()), responseService.getAll(rate),
						imageService.getImageByRate(rate)))
				.collect(Collectors.toList());

		RatePageResponseDto ratePageResponseDto = new RatePageResponseDto(ratePage.getTotalPages(),
				ratePage.getNumber(), ratePage.getSize(), rateResponseDtos);

		return ResponseEntity.ok(ratePageResponseDto);

	}

	@GetMapping(value = "/getByOrder")
	@ResponseBody
	public ResponseEntity<RateResponseDto> getProductByOrder(@RequestParam Integer orderId,
			@RequestParam Integer variantId) {
		Rate rate = rateService.finPageByOrderAndVariant(orderService.getOrderById(orderId).get(),
				variantService.getVariantById(variantId).get());

		RateResponseDto rateResponseDtos = new RateResponseDto();
		rateResponseDtos.setId(rate.getId());
		rateResponseDtos.setCreateAt(rate.getCreateAt());
		rateResponseDtos.setContent(rate.getContent());
		rateResponseDtos.setRating(rate.getRating());
		rateResponseDtos.setUserResponseDto(mapper.accountToAccountResponseDto(rate.getAccount()));
		rateResponseDtos.setResponses(responseService.getAll(rate));
		rateResponseDtos.setImages(imageService.getImageByRate(rate));

		return ResponseEntity.ok(rateResponseDtos);

	}
}
