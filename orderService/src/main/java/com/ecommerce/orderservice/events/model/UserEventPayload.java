package com.ecommerce.orderservice.events.model;

import lombok.Data;

@Data
public class UserEventPayload {
    private Long id;
    private String email;
    private String name;
    private String phone;
    // Add other fields as needed
}
