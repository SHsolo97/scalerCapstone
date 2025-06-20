package com.example.cartservice.models;

import lombok.Data;

@Data
public class CartItem {

    private String productId;
    private int quantity;
    private double price; // Price at the time of adding to cart (for historical pricing)
    private String productName; // Product name at the time of adding to cart

    public CartItem() {
    }

    public CartItem(String productId, int quantity, double price, String productName) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.productName = productName;
    }
}
