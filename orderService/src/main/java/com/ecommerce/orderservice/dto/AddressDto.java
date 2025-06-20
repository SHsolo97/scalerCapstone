package com.ecommerce.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Represents a postal address")
public class AddressDto {
    @Schema(description = "Street name and number", example = "123 Main St")
    private String street;

    @Schema(description = "City name", example = "Anytown")
    private String city;

    @Schema(description = "State or province", example = "CA")
    private String state;

    @Schema(description = "Postal or ZIP code", example = "90210")
    private String postalCode;

    @Schema(description = "Country name or code", example = "USA")
    private String country;
}
