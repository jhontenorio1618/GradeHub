package com.github.gradehub.dtos;

import com.github.gradehub.entities.Role;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class UserUpdateDTO {

    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String personFirstName;

    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String personLastName;

    @Email(message = "Invalid email format")
    private String email;

    @Size(min = 12, max = 100, message = "Password must be between 12 and 100 characters")
    private String password;

    private Role role;
}
