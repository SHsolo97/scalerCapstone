package ecommerce.emailservice.dtos;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendEmailEventDto {
    private String eventType = "user_registered";
    private String toEmail;
    private String fullName;
    private LocalDateTime registrationTimestamp;
}
