package com.example.productservice.dtos;

import com.example.productservice.models.Category;
import jakarta.persistence.ManyToOne;

public class ProductDto {
    String title;
    String description;
    String imageUrl;
    Category category;
    double price;
}
