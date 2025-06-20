package com.scaler.userservicemwfeve.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDto {
    private Long id;

    @NotBlank(message = "Street cannot be blank")
    @Size(max = 255, message = "Street must be less than 255 characters")
    private String street;

    @Size(max = 100, message = "Apartment must be less than 100 characters")
    private String apartment;

    @NotBlank(message = "City cannot be blank")
    @Size(max = 100, message = "City must be less than 100 characters")
    private String city;

    @NotBlank(message = "State cannot be blank")
    @Size(max = 100, message = "State must be less than 100 characters")
    private String state;

    @NotBlank(message = "Postal code cannot be blank")
    @Size(max = 20, message = "Postal code must be less than 20 characters")
    private String postalCode;

    @NotBlank(message = "Country cannot be blank")
    @Size(max = 100, message = "Country must be less than 100 characters")
    private String country;

    @NotBlank(message = "Address type cannot be blank")
    @Size(max = 50, message = "Address type must be less than 50 characters")
    private String addressType; // E.g., "HOME", "WORK"

    @NotNull(message = "User ID cannot be null for Create/Update operations")
    private Long userId; // Only used for request, will be set from path param or authenticated user
}
