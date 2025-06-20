package com.ecommerce.orderservice.events.model;

import lombok.Data;

@Data
public class AddressEventPayload {
    private Long id;
    private String street;
    private String apartment;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String addressType;
    private Long userId;
}
