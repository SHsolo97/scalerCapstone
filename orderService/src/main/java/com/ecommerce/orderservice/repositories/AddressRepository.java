package com.ecommerce.orderservice.repositories;

import com.ecommerce.orderservice.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
