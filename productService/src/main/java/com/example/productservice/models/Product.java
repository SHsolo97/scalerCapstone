package com.example.productservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;

@Getter
@Setter
@Entity
public class Product extends BaseModel {

    @NotBlank(message = "Title is mandatory")
    String title;

    @NotBlank(message = "Description is mandatory")
    String description;

    @NotBlank(message = "Image URL is mandatory")
    String imageUrl;

    @ManyToOne()
    @NotNull(message = "Category is mandatory")
    Category category;

    @Positive(message = "Price must be positive")
    double price;

    @Min(value = 0, message = "Quantity cannot be negative")
    int quantity;
}
