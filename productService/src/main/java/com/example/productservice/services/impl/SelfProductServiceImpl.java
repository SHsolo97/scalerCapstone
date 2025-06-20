package com.example.productservice.services.impl;

import com.example.productservice.dtos.CreateProductDto;
import com.example.productservice.exceptions.ProductNotExistsException;
import com.example.productservice.models.Category;
import com.example.productservice.models.Product;
import com.example.productservice.repositories.CategoryRepository;
import com.example.productservice.repositories.ProductRepository;
import com.example.productservice.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("selfProductService")
public class SelfProductServiceImpl implements ProductService {
    final private CategoryRepository categoryRepository;
    final private ProductRepository productRepository;
    SelfProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {

        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }
    @Override
    public Product getSingleProduct(Long id) throws ProductNotExistsException {
        return productRepository.getProductsByIdAndAndDeletedIsFalse(id)
                .orElseThrow(() -> new ProductNotExistsException("Product with id: " + id + " doesn't exist."));
    }

    @Override
    public Product updateProduct(Long id, Product product) throws ProductNotExistsException {
        return productRepository.getProductsByIdAndAndDeletedIsFalse(id)
                .orElseThrow(() -> new ProductNotExistsException("Product with id: " + id + " doesn't exist."));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.getAllByDeletedIsFalse();
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAllByDeletedIsFalse(pageable);
    }

    @Override
    public Product replaceProduct(Long id, Product product) throws ProductNotExistsException{
        Product selectedProduct = productRepository.getProductsByIdAndAndDeletedIsFalse(id)
                .orElseThrow(() -> new ProductNotExistsException("Product with id: " + id + " doesn't exist."));
        selectedProduct.setCategory(product.getCategory());
        selectedProduct.setDescription(product.getDescription());
        selectedProduct.setTitle(product.getTitle());
        selectedProduct.setPrice(product.getPrice());
        selectedProduct.setImageUrl(product.getImageUrl());
        productRepository.save(selectedProduct);
        return selectedProduct;
    }

    @Override
    public Product addNewProduct(CreateProductDto product) throws ProductNotExistsException {
        Product newProduct = new Product();
        Category category = categoryRepository.getCategoryByIdAndDeletedIsFalse(product.getCategoryId()).
                orElseThrow(() -> new ProductNotExistsException("Category Id: " + product.getCategoryId() + " doesn't exist."));
        newProduct.setCategory(category);
        newProduct.setDescription(product.getDescription());
        newProduct.setTitle(product.getTitle());
        newProduct.setPrice(product.getPrice());
        newProduct.setImageUrl(product.getImageUrl());
        productRepository.save(newProduct);
        return newProduct;
    }

    @Override
    public String deleteProduct(Long id) {
        Product product = productRepository.getProductsByIdAndAndDeletedIsFalse(id)
                .orElse(null);
        if (product != null) {
            product.setDeleted(true);
            productRepository.save(product);
            return "Product with id: " + id + " deleted successfully.";
        }
        return "Product with id: " + id + " doesn't exist.";
    }
}
