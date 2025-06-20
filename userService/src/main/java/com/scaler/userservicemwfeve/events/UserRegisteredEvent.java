package com.scaler.userservicemwfeve.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisteredEvent {
    private String eventType = "user_registered";
    private String toEmail;
    private String fullName;
    private LocalDateTime registrationTimestamp;

}