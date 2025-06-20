package com.ecommerce.orderservice.repositories;

import com.ecommerce.orderservice.models.Order;
import com.ecommerce.orderservice.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByUser(User user, Pageable pageable);
    Optional<Order> findByIdAndUser(Long id, User user);
}
