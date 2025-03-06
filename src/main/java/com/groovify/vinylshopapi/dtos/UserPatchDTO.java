package com.groovify.vinylshopapi.dtos;

import com.groovify.vinylshopapi.validation.ValidDate;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserPatchDTO {
    @Pattern(regexp = "^[a-zA-Z0-9._&@!]{3,50}$", message = "Username must be between 3 and 50 characters and may not contain spaces.")
    private String username;

    @Email(message = "Invalid email format")
    private String email;

    @Size(min = 1, max = 100, message = "First name cannot be longer than 100 characters")
    private String firstName;

    @Size(min = 1, max = 100, message = "Last name cannot be longer than 100 characters")
    private String lastName;

    @ValidDate(min = "1900-01-01", mustBePast = true, message = "Birth data must be in the past")
    private LocalDate dateOfBirth;

    @Pattern(regexp = "^(\\+\\d{1,3})?\\s?\\d{6,15}$", message = "Invalid phone number format")
    private String phone;
}
