package com.example.cartservice.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "carts")
@Data
public class Cart {

    @Id
    private String id; // Cart ID (e.g., could be same as userId for simplicity)
    private String userId; // User ID associated with the cart
    private List<CartItem> cartItems;

    // You can add more fields like creationTimestamp, lastUpdatedTimestamp, etc.

    public Cart() {
    }

    public Cart(String userId) {
        this.userId = userId;
    }
}