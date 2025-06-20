package com.example.productservice.services;

import com.example.productservice.models.Category;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CategoryService {
    String AddNewCategory(String categoryName);

    List<Category> getAllCategories();
}
