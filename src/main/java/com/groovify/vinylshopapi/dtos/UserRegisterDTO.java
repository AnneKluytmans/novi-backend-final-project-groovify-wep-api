package com.groovify.vinylshopapi.dtos;

import com.groovify.vinylshopapi.validation.ValidDate;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRegisterDTO {
    @NotBlank(message = "Username is required")
    @Pattern(regexp = "^[a-zA-Z0-9._&@!]{3,50}$", message = "Username must be between 3 and 50 characters and may not contain spaces.")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Password is required")
    @Size(min = 8, max = 200, message = "Password must be between 8 and 200 characters long")
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

    @Pattern(regexp = "^\\+?([0-9]{1,4})?\\s?(\\(?\\d{1,4}\\)?\\s?)?\\d{6,10}$", message = "Invalid phone number format")
    private String phone;
}
