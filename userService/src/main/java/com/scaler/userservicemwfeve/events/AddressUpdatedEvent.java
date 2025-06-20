package com.scaler.userservicemwfeve.events;

import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Getter
@Setter
public class AddressUpdatedEvent {
    private Long addressId;
    private Long userId;
    private String action = "ADDRESS_UPDATED";
    private Instant timestamp = Instant.now();
    private String street;
    private String apartment;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String addressType;
}
