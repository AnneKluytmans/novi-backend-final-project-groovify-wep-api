package com.groovify.vinylshopapi.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordDTO {
    @NotNull(message = "Old password is required")
    @Size(min = 8, max = 200, message = "Password must be between 8 and 200 characters long")
    private String oldPassword;

    @NotNull(message = "New password is required")
    @Size(min = 8, max = 200, message = "Password must be between 8 and 200 characters long")
    private String newPassword;

    @NotNull(message = "Confirm password is required")
    @Size(min = 8, max = 200, message = "Password must be between 8 and 200 characters long")
    private String confirmPassword;
}
