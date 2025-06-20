package com.example.cartservice.repositories;

import com.example.cartservice.models.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> { // String is the type of Cart ID
    Cart findByUserId(String userId); // Find cart by userId
}
