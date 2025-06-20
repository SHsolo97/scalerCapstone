package com.example.productservice.controllers;

import com.example.productservice.dtos.CreateCategoryDto;
import com.example.productservice.exceptions.ProductNotExistsException;
import com.example.productservice.models.Category;
import com.example.productservice.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @PostMapping()
    public ResponseEntity<String> addCategory(@RequestBody CreateCategoryDto category) {
        return new ResponseEntity<>(categoryService.AddNewCategory(category.getName()),
                HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<Category>> getAllCategories() {
        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
    }
    @ExceptionHandler(ProductNotExistsException.class)
    public ResponseEntity<String> handleProductNotExistException(String message) {
        return new ResponseEntity<>(message,
                HttpStatus.FORBIDDEN);
    }
}
