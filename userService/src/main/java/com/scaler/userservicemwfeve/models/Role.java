package com.scaler.userservicemwfeve.models;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Role extends BaseModel {
    @NotBlank(message = "Role name cannot be blank")
    @Size(max = 50, message = "Role name must be less than 50 characters")
    private String name;
}
