package com.example.productservice.dtos;

public class CreateCategoryDto {
    private String name;

    public CreateCategoryDto() {
    }

    public CreateCategoryDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
