package com.example.cartservice.services.impl;

import com.example.cartservice.models.Cart;
import com.example.cartservice.models.CartItem;
import com.example.cartservice.repositories.CartRepository;
import com.example.cartservice.services.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate; // For Kafka events
    private final ObjectMapper objectMapper; // For JSON serialization (optional, for Kafka message payload)

    private static final String CART_CACHE_KEY_PREFIX = "cart:";
    private static final String CART_EVENTS_TOPIC = "cart_events";

    @Autowired
    public CartServiceImpl(CartRepository cartRepository,
                           RedisTemplate<String, Object> redisTemplate,
                           KafkaTemplate<String, String> kafkaTemplate,
                           ObjectMapper objectMapper) {
        this.cartRepository = cartRepository;
        this.redisTemplate = redisTemplate;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public Cart getCartByUserId(String userId) {
        String cacheKey = CART_CACHE_KEY_PREFIX + userId;
        Cart cart = (Cart) redisTemplate.opsForValue().get(cacheKey); // Try to get from cache first
        if (cart != null) {
            log.info("Cart retrieved from cache for userId: {}", userId);
            return cart;
        }

        cart = cartRepository.findByUserId(userId);
        if (cart != null) {
            redisTemplate.opsForValue().set(cacheKey, cart); // Cache the cart if found in DB
            log.info("Cart retrieved from DB and cached for userId: {}", userId);
        } else {
            cart = createNewCartForUser(userId); // Create a new cart if not found
            log.info("New cart created for userId: {}", userId);
        }
        return cart;
    }

    private Cart createNewCartForUser(String userId) {
        Cart newCart = new Cart(userId);
        return cartRepository.save(newCart);
    }

    @Override
    public Cart addItemToCart(String userId, String productId, int quantity, double price, String productName) {
        Cart cart = getCartByUserId(userId);
        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        Optional<CartItem> existingItem = cartItems.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem(productId, quantity, price, productName);
            cartItems.add(newItem);
        }

        cart.setCartItems(cartItems);
        Cart updatedCart = cartRepository.save(cart);
        clearCache(userId); // Invalidate cache after update
        publishCartEvent("item_added_to_cart", userId, productId, quantity, updatedCart.getId()); // Publish Kafka event
        log.info("Item added to cart for userId: {}, productId: {}", userId, productId);
        return updatedCart;
    }

    @Override
    public Cart updateCartItemQuantity(String userId, String productId, int quantity) {
        Cart cart = getCartByUserId(userId);
        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems == null) {
            return cart; // Or throw exception, cart is empty
        }

        boolean itemUpdated = false;
        for (CartItem item : cartItems) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(quantity);
                itemUpdated = true;
                break;
            }
        }

        if (!itemUpdated) {
            return cart; // Or throw exception, item not found in cart
        }

        cart.setCartItems(cartItems);
        Cart updatedCart = cartRepository.save(cart);
        clearCache(userId); // Invalidate cache after update
        publishCartEvent("cart_item_quantity_updated", userId, productId, quantity, updatedCart.getId()); // Publish Kafka event
        log.info("Cart item quantity updated for userId: {}, productId: {}, quantity: {}", userId, productId, quantity);
        return updatedCart;
    }

    @Override
    public Cart removeCartItem(String userId, String productId) {
        Cart cart = getCartByUserId(userId);
        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems == null || cartItems.isEmpty()) {
            return cart; // Cart is already empty
        }

        cartItems.removeIf(item -> item.getProductId().equals(productId)); // Remove item if productId matches

        cart.setCartItems(cartItems);
        Cart updatedCart = cartRepository.save(cart);
        clearCache(userId); // Invalidate cache after update
        publishCartEvent("cart_item_removed", userId, productId, 0, updatedCart.getId()); // Quantity 0 for remove event
        log.info("Cart item removed for userId: {}, productId: {}", userId, productId);
        return updatedCart;
    }

    @Override
    public Cart clearCart(String userId) {
        Cart cart = getCartByUserId(userId);
        cart.setCartItems(new ArrayList<>()); // Clear the cart items
        Cart updatedCart = cartRepository.save(cart);
        clearCache(userId); // Invalidate cache after update
        publishCartEvent("cart_cleared", userId, "", 0, updatedCart.getId()); // ProductId empty for clear event
        log.info("Cart cleared for userId: {}", userId);
        return updatedCart;
    }

    private void clearCache(String userId) {
        String cacheKey = CART_CACHE_KEY_PREFIX + userId;
        redisTemplate.delete(cacheKey);
        log.info("Cart cache cleared for userId: {}", userId);
    }

    private void publishCartEvent(String eventType, String userId, String productId, int quantity, String cartId) {
        try {
            String messagePayload = objectMapper.writeValueAsString(
                    new CartEvent(eventType, userId, productId, quantity, cartId, System.currentTimeMillis())
            );
            kafkaTemplate.send(CART_EVENTS_TOPIC, messagePayload);
            log.info("Kafka event published to topic '{}': {}", CART_EVENTS_TOPIC, messagePayload);
        } catch (Exception e) {
            log.error("Error publishing Kafka event", e);
        }
    }

    @Data
    static class CartEvent {
        private String eventType;
        private String userId;
        private String productId;
        private int quantity;
        private String cartId;
        private long timestamp;

        public CartEvent(String eventType, String userId, String productId, int quantity, String cartId, long timestamp) {
            this.eventType = eventType;
            this.userId = userId;
            this.productId = productId;
            this.quantity = quantity;
            this.cartId = cartId;
            this.timestamp = timestamp;
        }
    }
}
