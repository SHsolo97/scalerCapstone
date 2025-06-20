package com.example.productservice.controllers;

import com.example.productservice.dtos.CreateProductDto;
import com.example.productservice.exceptions.ProductNotExistsException;
import com.example.productservice.models.Product;
import com.example.productservice.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private ProductService productService;

    @Autowired
    public ProductController(@Qualifier("selfProductService") ProductService productService) {
        this.productService = productService;
    }

    @GetMapping() // localhost:8080/products
    public ResponseEntity<Page<Product>> getAllProducts(Pageable pageable) {
        return new ResponseEntity<>(
                productService.getAllProducts(pageable), HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getSingleProduct(@PathVariable("id") Long id) throws ProductNotExistsException {
        return new ResponseEntity<>(
                productService.getSingleProduct(id),
                HttpStatus.OK
        );
    }

    @PostMapping()
    public ResponseEntity<Product> addNewProduct(@RequestBody CreateProductDto product) throws ProductNotExistsException {
        return new ResponseEntity<>(productService.addNewProduct(product), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id, @RequestBody Product product) throws ProductNotExistsException {
        return new ResponseEntity<>(productService.updateProduct(id, product), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public Product replaceProduct(@PathVariable("id") Long id, @RequestBody Product product) {
        return new Product();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id) {
        return new ResponseEntity<>(productService.deleteProduct(id), HttpStatus.OK);
    }

    @ExceptionHandler(ProductNotExistsException.class)
    public ResponseEntity<String> handleProductNotExistException(String message) {
        return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
    }
}