package com.groovify.vinylshopapi.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmployeeRegisterDTO extends UserRegisterDTO {
    @NotNull(message = "Job title is required")
    @Size(min = 3, max = 50, message = "Job title must be between 3 and 50 characters")
    private String jobTitle;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be a positive value")
    private Double salary;

    @NotNull(message = "Work hours are required")
    @Positive(message = "Work hours must be a positive value")
    private Integer workHours;

    private Boolean isAdmin = false;
}
