package com.groovify.vinylshopapi.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EmployeeAdminPatchDTO {
    @Size(min = 1, max = 50, message = "Job title cannot be longer than 50 characters")
    private String jobTitle;

    @Positive(message = "Salary must be a positive value")
    private Double salary;

    @Positive(message = "Work hours must be a positive value")
    private Integer workHours;
}
