package com.example.cartservice.services;

import com.example.cartservice.models.Cart;

public interface CartService {
    Cart getCartByUserId(String userId);
    Cart addItemToCart(String userId, String productId, int quantity, double price, String productName);
    Cart updateCartItemQuantity(String userId, String productId, int quantity);
    Cart removeCartItem(String userId, String productId);
    Cart clearCart(String userId);
}
