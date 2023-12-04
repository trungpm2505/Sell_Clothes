package com.web.SellShoes.mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.web.SellShoes.dto.requestDto.CartRequestDto;
import com.web.SellShoes.dto.requestDto.ProductRequestDto;
import com.web.SellShoes.dto.requestDto.PromotionRequestDto;
import com.web.SellShoes.dto.requestDto.RateRequestDto;
import com.web.SellShoes.dto.requestDto.UserRequestDto;
import com.web.SellShoes.dto.requestDto.VariantRequestDto;
import com.web.SellShoes.dto.responseDto.AccountResponseDto;
import com.web.SellShoes.dto.responseDto.CartResponseDto;
import com.web.SellShoes.dto.responseDto.OrderDetailResponseDto;
import com.web.SellShoes.dto.responseDto.OrderResponseDto;
import com.web.SellShoes.dto.responseDto.ProductResponseDto;
import com.web.SellShoes.dto.responseDto.VariantResponseDto;
import com.web.SellShoes.entity.Account;
import com.web.SellShoes.entity.Brand;
import com.web.SellShoes.entity.Cart;
import com.web.SellShoes.entity.Category;
import com.web.SellShoes.entity.Color;
import com.web.SellShoes.entity.Order;
import com.web.SellShoes.entity.OrderDetail;
import com.web.SellShoes.entity.Product;
import com.web.SellShoes.entity.Promotion;
import com.web.SellShoes.entity.Rate;
import com.web.SellShoes.entity.Size;
import com.web.SellShoes.entity.Variant;
import com.web.SellShoes.service.AccountService;
import com.web.SellShoes.service.BrandService;
import com.web.SellShoes.service.CartService;
import com.web.SellShoes.service.CategoryService;
import com.web.SellShoes.service.ColorService;
import com.web.SellShoes.service.ImageService;
import com.web.SellShoes.service.OrderService;
import com.web.SellShoes.service.ProductService;
import com.web.SellShoes.service.RateService;
import com.web.SellShoes.service.SizeService;
import com.web.SellShoes.service.VariantService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Mapper {
	private final CategoryService categoryService;
	private final ProductService productService;
	private final BrandService brandService;
	private final ColorService colorService;
	private final SizeService sizeService;
	private final ImageService imageService;
	private final VariantService variantService;
	private final RateService rateService;
	private final OrderService orderService;
	private final AccountService accountService;
	private final CartService cartService;

	public Account userRquestDtoMapToUser(UserRequestDto userRequestDto) {
		Account user = new Account();
		user.setPassword(userRequestDto.getPassword());
		user.setFullName(userRequestDto.getFullname());
		user.setAddress(userRequestDto.getAddress());
		user.setPhone(userRequestDto.getPhone());
		user.setEmail(userRequestDto.getEmail());
		return user;
	}

	public Product productRequestDtoToProduct(ProductRequestDto productRequestDto) {
		Product product = new Product();
		if (productRequestDto.getId() != null) {
			product.setId(productRequestDto.getId());
		}
		// set title
		product.setTitle(productRequestDto.getTitle());

		// set category
		Optional<Category> category = categoryService.getCategory(productRequestDto.getCategory());
		product.setCategory(category.get());

		// set Brand
		Optional<Brand> brand = brandService.getBrand(productRequestDto.getBrand());
		product.setBrand(brand.get());

		// set discription
		product.setDiscription(productRequestDto.getDiscription());

		return product;

	}

	public CartResponseDto cartToCartResponseDto(Cart cart) {
		CartResponseDto cartResponseDto = new CartResponseDto();
		// variant = variantService.getVariantById(cart.getAccount().getId());
		Optional<Variant> variant = variantService.getVariantById(cart.getVariant().getId());
		Optional<Product> product = productService.getProductById(variant.get().getProduct().getId());
		cartResponseDto.setVariantResponseDto(variantToVariantResponese(variant.get()));
		cartResponseDto.setProductResponseDto(productToProductResponese(product.get()));
		cartResponseDto.setCartId(cart.getId());
		cartResponseDto.setQuantity(cart.getQuantity());
		cartResponseDto.setPrice(variant.get().getPrice());
		cartResponseDto.setCurrentPrice(variant.get().getCurrentPrice());
		if (variant.get().getCurrentPrice() != null) {
			cartResponseDto.setTotal(cart.getQuantity() * variant.get().getCurrentPrice());
		} else {
			cartResponseDto.setTotal(cart.getQuantity() * variant.get().getPrice());
		}
		cartResponseDto.setImages(imageService.getImageByProductAndDefault(product.get()));
		return cartResponseDto;
	}

	public Cart cartRequestDtoToCart(CartRequestDto cartRequestDto, HttpSession httpSession) {
		String email = (String) httpSession.getAttribute("email");
		Optional<Account> account = accountService.findUserByEmail("trungpmpd05907@fpt.edu.vn");
		List<Cart> findByUser = cartService.findCartByAccount(account.get());
		Optional<Variant> variant = variantService.getVariantById(cartRequestDto.getVariantId());
		Optional<Product> product = productService.getProductById(variant.get().getProduct().getId());
		Optional<Cart> cartExit = cartService.findCartByVariantAndAccount(variant.get(), account.get());
		if (cartExit.isPresent()) {
			if (cartRequestDto.getQuantity() != 0) {
				cartExit.get().setQuantity(cartExit.get().getQuantity() + cartRequestDto.getQuantity());
			} else {
				cartExit.get().setQuantity(cartExit.get().getQuantity() + 1);
			}
			cartExit.get().setUpdatedAt(LocalDateTime.now());
			return cartExit.get();
		} else {
			Cart cart = new Cart();
			if (cartRequestDto.getQuantity() != 0) {
				cart.setQuantity(cartRequestDto.getQuantity());

			} else {
				cart.setQuantity(1);
			}

			cart.setVariant(variant.get());
			cart.setAccount(account.get());
			return cart;
		}

	}

	public Cart cartRequestDtoToCart(CartRequestDto cartRequestDto) {

		Optional<Variant> variant = variantService.getVariantById(cartRequestDto.getVariantId());
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<Account> account = accountService.findUserByEmail(authentication.getName());
		//Optional<Account> account = accountService.findUserByEmail("trungpmpd05907@fpt.edu.vn");

		// if product and user id is exist in cart -> update
		Optional<Cart> cartExist = cartService.findCartByVariantAndAccount(variant.get(), account.get());
		if (cartExist.isPresent()) {
			if (cartRequestDto.getQuantity() != 0) {
				cartExist.get().setQuantity(cartExist.get().getQuantity() + cartRequestDto.getQuantity());
			} else {
				cartExist.get().setQuantity(cartExist.get().getQuantity() + 1);
			}
			// set update at
			cartExist.get().setUpdatedAt(LocalDateTime.now());
			return cartExist.get();
		} else {
			Cart cart = new Cart();
			// set Number
			if (cartRequestDto.getQuantity() != 0) {
				cart.setQuantity(cartRequestDto.getQuantity());
			} else {
				cart.setQuantity(1);
			}

			// set Product
			cart.setVariant(variant.get());

			// set user
			cart.setAccount(account.get());
			return cart;
		}

	}

	public VariantResponseDto variantToVariantResponese(Variant variant) {

		VariantResponseDto variantResponseDto = new VariantResponseDto();

		variantResponseDto.setId(variant.getId());
		variantResponseDto.setQuantity(variant.getQuantity());
		variantResponseDto.setTitle(variant.getProduct().getTitle());
		variantResponseDto.setSize(variant.getSize().getSize());
		variantResponseDto.setColor(variant.getColor().getColor());
		variantResponseDto.setPrice(variant.getPrice());
		variantResponseDto.setCurrentPrice(variant.getCurrentPrice());
		variantResponseDto.setProductId(variant.getProduct().getId());
		return variantResponseDto;
	}

	public ProductResponseDto productToProductResponese2(Product product) {
		ProductResponseDto productResponseDto = new ProductResponseDto();

		productResponseDto.setImages(imageService.getImageByProduct(product));
		productResponseDto.setTitle(product.getTitle());

		return productResponseDto;
	}

	public Variant variantRequestDtoToVariant(VariantRequestDto variantRequestDto) {
		Variant variant = new Variant();

		if (variantRequestDto.getVariantId() != null) {
			variant.setId(variantRequestDto.getVariantId());
		}

		Optional<Product> product = productService.getProductById(variantRequestDto.getProduct());
		variant.setProduct(product.get());

		Optional<Color> color = colorService.getColor(variantRequestDto.getColor());
		variant.setColor(color.get());

		Optional<Size> size = sizeService.getSize(variantRequestDto.getSize());
		variant.setSize(size.get());

		variant.setPrice(variantRequestDto.getPrice());
		if (variantRequestDto.getCurrentPrice() != 0) {
			variant.setCurrentPrice(variantRequestDto.getCurrentPrice());
		}

		variant.setQuantity(variantRequestDto.getQuantity());

		if (variantRequestDto.getNote() != null) {
			variant.setNote(variantRequestDto.getNote());
		}

		return variant;

	}

	public Promotion promotionRequestDtoToPromotion(PromotionRequestDto promotionRequestDto) {
		Promotion promotion = new Promotion();
		if (promotionRequestDto.getId() != null) {
			promotion.setId(promotionRequestDto.getId());
		}

		promotion.setCouponCode(promotionRequestDto.getCouponCode());
		promotion.setName(promotionRequestDto.getName());
		promotion.setActive(promotionRequestDto.isActive());
		promotion.setPublic(promotionRequestDto.isPublic());
		promotion.setExpiredAt(promotionRequestDto.getExpiredDate());
		promotion.setDiscountType(promotionRequestDto.getDiscountType());
		promotion.setDiscountValue(promotionRequestDto.getDiscountValue());
		if (promotionRequestDto.getDiscountType() == 1) {
			promotion.setMaximumDiscountValue(promotionRequestDto.getMaxValue());
		} else {
			promotion.setMaximumDiscountValue(promotionRequestDto.getDiscountValue());
		}

		return promotion;

	}

	public ProductResponseDto productToProductResponese(Product product) {
		ProductResponseDto productResponse = new ProductResponseDto();

		productResponse.setId(product.getId());

		// set title
		productResponse.setTitle(product.getTitle());

		// set description
		productResponse.setDiscription(product.getDiscription());

		// set category
		Optional<Category> category = categoryService.getCategory(product.getCategory().getId());
		productResponse.setCategoryName(category.get().getCategoryName());

		Optional<Brand> brand = brandService.getBrand(product.getBrand().getId());
		productResponse.setBrandName(brand.get().getName());

		// set variant
		productResponse.setVariantResponseDtos(variantService.getVariantByProduct(product));

		// set images
		productResponse.setImages(imageService.getImageByProduct(product));

		return productResponse;

	}

	public Rate mapRateRequestDtoToRate(RateRequestDto rateRequestDto, Account account) {
		Rate rate = null;
		if (rateRequestDto.getRateId() != null) {
			rate = rateService.getRateById(rateRequestDto.getRateId());
			rate.setRating(rateRequestDto.getRating());
			rate.setContent(rateRequestDto.getContent());

		} else {
			rate = new Rate();
			rate.setAccount(account);
			rate.setVariant(variantService.getVariantById(rateRequestDto.getVariantId()).get());
			rate.setRating(rateRequestDto.getRating());
			rate.setContent(rateRequestDto.getContent());
			rate.setOrder(orderService.getOrderById(rateRequestDto.getOrderId()).get());
		}

		return rate;
	}

	public AccountResponseDto accountToAccountResponseDto(Account account) {
		AccountResponseDto accountResponseDto = new AccountResponseDto();
		accountResponseDto.setId(account.getId());
		accountResponseDto.setFullName(account.getFullName());
		accountResponseDto.setAddress(account.getAddress());
		accountResponseDto.setPhone(account.getPhone());
		accountResponseDto.setEmail(account.getEmail());
		accountResponseDto.setRoleName(account.getRole().getRoleName());
		return accountResponseDto;

	}

	public List<OrderDetailResponseDto> listOrderDtailsToListOrderDetailsDto(List<OrderDetail> OrderDetailsList) {
		List<OrderDetailResponseDto> orderDetailsResponseDtos = new ArrayList<>();
		for (OrderDetail orderDetails : OrderDetailsList) {
			OrderDetailResponseDto orderDetailsResponseDto = new OrderDetailResponseDto();

			orderDetailsResponseDto.setNumber(orderDetails.getQuantity());
			orderDetailsResponseDto.setPrice(orderDetails.getPrice());

			if (orderDetails.getCurentPrice() != 0) {
				orderDetailsResponseDto.setCurentPrice(orderDetails.getCurentPrice());
			}
			orderDetailsResponseDto.setVariantResponseDto(variantToVariantResponese(orderDetails.getVariant()));

			orderDetailsResponseDto.setTotalMoney(orderDetails.getTotalMoney());
			orderDetailsResponseDto.setImageForSave(imageService
					.getImageByProductAndDefault(orderDetails.getVariant().getProduct()).getInmageForSave());
			orderDetailsResponseDtos.add(orderDetailsResponseDto);
		}
		return orderDetailsResponseDtos;
	}

	public OrderResponseDto orderToOrderResponseDto(Order order) {
		OrderResponseDto orderResponseDto = new OrderResponseDto();
		orderResponseDto.setId(order.getId());
		orderResponseDto.setFullName(order.getFullName());
		orderResponseDto.setPhone(order.getPhone_Number());
		orderResponseDto.setAddress(order.getAdrress());
		orderResponseDto.setCreateAt(order.getOrder_date());
		orderResponseDto.setCompletedAt(order.getCompletedAt());
		orderResponseDto.setTotalMoney(order.getTotalMoney());
		orderResponseDto.setStatus(order.getStatus());
		orderResponseDto.setNote(order.getNote());
		return orderResponseDto;
	}
}
