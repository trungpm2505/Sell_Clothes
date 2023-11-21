package com.web.SellShoes.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.SellShoes.dto.requestDto.CartRequestDto;
import com.web.SellShoes.dto.responseDto.CartResponseDto;
import com.web.SellShoes.dto.responseDto.ProductResponseDto;
import com.web.SellShoes.dto.responseDto.VariantResponseDto;
import com.web.SellShoes.entity.Account;
import com.web.SellShoes.entity.Cart;
import com.web.SellShoes.entity.Category;
import com.web.SellShoes.entity.Image;
import com.web.SellShoes.entity.Product;
import com.web.SellShoes.entity.Variant;
import com.web.SellShoes.repository.CartRepository;
import com.web.SellShoes.service.AccountService;
import com.web.SellShoes.service.CartService;
import com.web.SellShoes.service.ImageService;
import com.web.SellShoes.service.ProductService;
import com.web.SellShoes.service.VariantService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/cart")
public class CartController {
	private final CartService cartService;
	private final AccountService accountService;
	private final VariantService variantService;
	private final ProductService productService;
	private final ImageService imageService;
	//private final Mapper mapper;

	@GetMapping()
	public String view() {
		return "shop/shopcontent/cart";
	}

	@GetMapping(value = "/findAll")
	public ResponseEntity<List<CartResponseDto>> findAllByUser(HttpSession session) {

		String email = (String) session.getAttribute("email");
		Optional<Account> account = accountService.findUserByEmail("an@gmail.com");

		List<Cart> findByUser = cartService.findCartByAccount(account.get());
		List<CartResponseDto> cartResponseDtos = new ArrayList<>();
		CartResponseDto cartResponseDto = null;

		for (Cart cart : findByUser) {
			cartResponseDto = cartToCartResponseDto(cart);

			cartResponseDtos.add(cartResponseDto);
		}

		return ResponseEntity.ok().body(cartResponseDtos);
	}



	public CartResponseDto cartToCartResponseDto(Cart cart) {
		CartResponseDto cartResponseDto = new CartResponseDto();
		//variant = variantService.getVariantById(cart.getAccount().getId());
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
		return cartResponseDto;
	}
	public Cart cartRequestDtoToCart(CartRequestDto cartRequestDto,HttpSession httpSession) {
		String email = (String) httpSession.getAttribute("email");
		Optional<Account> account = accountService.findUserByEmail("an@gmail.com");
		List<Cart> findByUser = cartService.findCartByAccount(account.get());
		Optional<Variant> variant = variantService.getVariantById(cartRequestDto.getVariantId());
	    Optional<Product> product = productService.getProductById(variant.get().getProduct().getId());
	    Optional<Cart>cartExit=cartService.findCartByVariantAndAccount(variant.get(),account.get());
	    if (cartExit.isPresent()) {
			if (cartRequestDto.getQuantity()!=0) {
				cartExit.get().setQuantity(cartExit.get().getQuantity()+cartRequestDto.getQuantity());
			}else {
				cartExit.get().setQuantity(cartExit.get().getQuantity()+1);
			}
			cartExit.get().setUpdatedAt(LocalDateTime.now());
			return cartExit.get();
		}else {
			Cart cart=new Cart();
			if (cartRequestDto.getQuantity()!=0) {
				cart.setQuantity(cartRequestDto.getQuantity());
				
			}else {
				cart.setQuantity(1);
			}
			
			cart.setVariant(variant.get());
			cart.setAccount(account.get());
			return cart;
		}
		
	}

	public VariantResponseDto variantToVariantResponese(Variant variant) {


		VariantResponseDto variantResponseDto = new VariantResponseDto();

		variantResponseDto.setId(variant.getId());
		variantResponseDto.setQuantity(variant.getQuantity());	
	    variantResponseDto.setSize(variant.getSize().getSize());
		variantResponseDto.setColor(variant.getColor().getColor());
		variantResponseDto.setPrice(variant.getPrice());
		variantResponseDto.setCurrentPrice(variant.getCurrentPrice());
		variantResponseDto.setProductId(variant.getProduct().getId());
		return variantResponseDto;
	}
	public ProductResponseDto productToProductResponese(Product product) {
		ProductResponseDto productResponseDto = new ProductResponseDto();

		productResponseDto.setImages(imageService.getImageByProduct(product));
	    productResponseDto.setTitle(product.getTitle());
	   
	
		return productResponseDto;
	}

	@PostMapping(value = "/updateQuantity")
	@ResponseBody
	public ResponseEntity<String> updateQuantity(@RequestParam Integer cartId,
	                                            @RequestParam Integer quantity) {
	    Optional<Cart> cart = cartService.getCartById(cartId);
	    if (cart.isEmpty()) {
	        return ResponseEntity.badRequest().body("Sản phẩm này không tồn tại trong giỏ hàng của bạn!");
	    }
	    
	    // Lấy biến thể tương ứng với mục trong giỏ hàng
	    Variant variant = cart.get().getVariant();
	    if (variant.getQuantity() < quantity) {
	        return ResponseEntity.badRequest().body("Số lượng yêu cầu vượt quá số lượng tồn kho của biến thể!");
	    }
	    
	    // Cập nhật số lượng trong giỏ hàng
	    cart.get().setQuantity(quantity);
	    cartService.save(cart.get());

	    return ResponseEntity.ok("Cập nhật số lượng thành công");
	}

	
	@PostMapping(value="/deleteCart")
	@ResponseBody
	public ResponseEntity<?>deleteCart(@RequestParam Integer cartId){
		Optional<Cart>cart=cartService.getCartById(cartId);
		if (cart.isEmpty()) {
			return ResponseEntity.badRequest().body("This product does not exist in your cart!");
		}
		cartService.deleteCart(cart.get().getId());
		return ResponseEntity.ok("Cart deleted successfully");
	}
	@PostMapping(value="/deleteAllCart")
	@ResponseBody
	public ResponseEntity<?> deleteAllCart() {
	    cartService.deleteAllCart();
	    return ResponseEntity.ok("All carts deleted successfully");
	}

}
