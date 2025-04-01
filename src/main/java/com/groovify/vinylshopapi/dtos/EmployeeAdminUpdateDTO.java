package com.groovify.vinylshopapi.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EmployeeAdminUpdateDTO {
    @NotBlank(message = "Job title is required")
    @Size(max = 50, message = "Job title cannot be longer than 50 characters")
    private String jobTitle;

    @NotNull(message = "Salary is required")
    @DecimalMin(value = "0.00", message = "Salary must be positive")
    private BigDecimal salary;

    @NotNull(message = "Work hours is required")
    @Positive(message = "Work hours must be a positive value")
    private Integer workHours;
}
