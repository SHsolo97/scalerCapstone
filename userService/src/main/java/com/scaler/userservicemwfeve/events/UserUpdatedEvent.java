package com.scaler.userservicemwfeve.events;

import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Getter
@Setter
public class UserUpdatedEvent {
    private Long userId;
    private String email;
    private String fullName;
    private String action = "USER_UPDATED";
    private Instant timestamp = Instant.now();
}
