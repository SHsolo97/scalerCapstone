package com.example.productservice.services;

import com.example.productservice.dtos.CreateProductDto;
import com.example.productservice.exceptions.ProductNotExistsException;
import com.example.productservice.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public interface ProductService {
    Product getSingleProduct(Long id) throws ProductNotExistsException;
    Product updateProduct(Long id, Product product) throws ProductNotExistsException;
    List<Product> getAllProducts();
    Product replaceProduct(Long id, Product product) throws ProductNotExistsException ;

    Product addNewProduct(CreateProductDto product) throws ProductNotExistsException;
    String deleteProduct(Long id);

    default Page<Product> getAllProducts(Pageable pageable) {
        List<Product> all = getAllProducts();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), all.size());
        return new PageImpl<>(all.subList(start, end), pageable, all.size());
    }
}
