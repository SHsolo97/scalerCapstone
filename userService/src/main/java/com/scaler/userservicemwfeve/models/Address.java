package com.scaler.userservicemwfeve.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

@Getter
@Setter
@Entity(name = "addresses")
@Where(clause = "deleted = false")
public class Address extends BaseModel {

    @NotBlank(message = "Street cannot be blank")
    @Size(max = 255, message = "Street must be less than 255 characters")
    private String street;

    @Size(max = 100, message = "Apartment must be less than 100 characters")
    private String apartment; // e.g., Apt 101, Suite B

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
    private String addressType; // E.g., "HOME", "WORK", "SHIPPING", "BILLING"

    @ManyToOne(optional = false)
    private User user;
}
