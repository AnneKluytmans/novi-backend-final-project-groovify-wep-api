package com.groovify.vinylshopapi.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReactivateUserDTO {
    @NotNull(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "VerificationCode is required")
    private String verificationCode;
}
