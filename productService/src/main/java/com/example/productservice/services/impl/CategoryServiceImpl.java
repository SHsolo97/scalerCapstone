package com.example.productservice.services.impl;

import com.example.productservice.models.Category;
import com.example.productservice.repositories.CategoryRepository;
import com.example.productservice.services.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public String  AddNewCategory(String categoryName) {
        if(categoryName == null || categoryName.isEmpty())
            return "Category name cannot be empty";
        Category category = new Category();
        category.setName(categoryName);
        try {
            categoryRepository.save(category);
        } catch (Exception e) {
            return "Category needs to be unique";
        }
        return "Category added";
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.getAllByDeletedIsFalse();
    }
}
