package com.ecommerce.orderservice.mapper;

import com.ecommerce.orderservice.models.User;
import com.ecommerce.orderservice.events.model.UserEventPayload;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntity(UserEventPayload payload) {
        if (payload == null) return null;
        User user = new User();
        user.setId(payload.getId());
        user.setEmail(payload.getEmail());
        user.setName(payload.getName());
        user.setPhone(payload.getPhone());
        return user;
    }
}
