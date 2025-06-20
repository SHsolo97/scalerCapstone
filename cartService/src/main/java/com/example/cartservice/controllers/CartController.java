package com.example.cartservice.controllers;

import com.example.cartservice.models.Cart;
import com.example.cartservice.services.CartService;
import com.example.cartservice.services.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final SecurityService securityService;

    @Autowired
    public CartController(CartService cartService, SecurityService securityService) {
        this.cartService = cartService;
        this.securityService = securityService;
    }

    @GetMapping
    public ResponseEntity<Cart> getCart(Authentication authentication) {
        Long userId = securityService.getUserId(authentication);
        Cart cart = cartService.getCartByUserId(String.valueOf(userId));
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PostMapping("/items")
    public ResponseEntity<Cart> addItem(Authentication authentication,
                                        @RequestParam("productId") String productId,
                                        @RequestParam("quantity") int quantity,
                                        @RequestParam("price") double price,
                                        @RequestParam("productName") String productName) {
        Long userId = securityService.getUserId(authentication);
        Cart updatedCart = cartService.addItemToCart(String.valueOf(userId), productId, quantity, price, productName);
        return new ResponseEntity<>(updatedCart, HttpStatus.CREATED);
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<Cart> updateQuantity(Authentication authentication,
                                               @PathVariable("productId") String productId,
                                               @RequestParam("quantity") int quantity) {
        Long userId = securityService.getUserId(authentication);
        Cart updatedCart = cartService.updateCartItemQuantity(String.valueOf(userId), productId, quantity);
        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Cart> removeItem(Authentication authentication,
                                           @PathVariable("productId") String productId) {
        Long userId = securityService.getUserId(authentication);
        Cart updatedCart = cartService.removeCartItem(String.valueOf(userId), productId);
        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Cart> clearCart(Authentication authentication) {
        Long userId = securityService.getUserId(authentication);
        Cart clearedCart = cartService.clearCart(String.valueOf(userId));
        return new ResponseEntity<>(clearedCart, HttpStatus.OK);
    }
}
