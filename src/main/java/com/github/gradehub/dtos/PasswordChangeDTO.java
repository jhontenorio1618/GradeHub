package com.github.gradehub.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordChangeDTO {

    private String oldPassword;

    @Size(min = 12, max = 100, message = "Password must be between 12 and 100 characters")
    private String newPassword;
}
