package com.ecommerce.orderservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity(name = "users")
public class User extends BaseModel {
    private String email;
    private String name;
    private String phone;
    // Add other fields as needed

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses;
}
