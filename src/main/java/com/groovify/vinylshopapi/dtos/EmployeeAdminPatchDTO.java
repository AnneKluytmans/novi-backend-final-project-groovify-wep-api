package com.groovify.vinylshopapi.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EmployeeAdminPatchDTO {
    @Size(min = 3, max = 50, message = "Job title must be between 3 and 50 characters")
    private String jobTitle;

    @Positive(message = "Salary must be a positive value")
    private Double salary;

    @Positive(message = "Work hours must be a positive value")
    private Integer workHours;

}
