package com.ecommerce.orderservice.repositories;

import com.ecommerce.orderservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
