package com.groovify.vinylshopapi.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EmployeeAdminUpdateDTO {
    @NotBlank(message = "Job title is required")
    @Size(max = 50, message = "Job title cannot be longer than 50 characters")
    private String jobTitle;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be a positive value")
    private Double salary;

    @NotNull(message = "Work hours is required")
    @Positive(message = "Work hours must be a positive value")
    private Integer workHours;
}
