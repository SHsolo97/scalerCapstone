package com.example.productservice.repositories;

import com.example.productservice.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> getProductsByIdAndAndDeletedIsFalse(long id);
    List<Product> getAllByDeletedIsFalse();
    Page<Product> findAllByDeletedIsFalse(Pageable pageable);

}
