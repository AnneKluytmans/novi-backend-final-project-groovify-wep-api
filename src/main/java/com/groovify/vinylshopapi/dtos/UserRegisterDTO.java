package com.groovify.vinylshopapi.dtos;

import com.groovify.vinylshopapi.validation.ValidDate;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRegisterDTO {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name cannot be longer than 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name cannot be longer than 100 characters")
    private String lastName;

    @NotNull(message = "Birth date is required")
    @ValidDate(min = "1900-01-01", mustBePast = true, message = "Birth data must be in the past")
    private LocalDate dateOfBirth;

    @Pattern(regexp = "^(\\+\\d{1,3})?\\s?\\d{6,15}$", message = "Invalid phone number format")
    private String phone;
}
