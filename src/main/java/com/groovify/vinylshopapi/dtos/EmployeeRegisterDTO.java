package com.groovify.vinylshopapi.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmployeeRegisterDTO extends UserRegisterDTO {
    @NotBlank(message = "Job title is required")
    @Size(max = 50, message = "Job title cannot be longer than 50 characters")
    private String jobTitle;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be a positive value")
    private BigDecimal salary;

    @NotNull(message = "Work hours are required")
    @Positive(message = "Work hours must be a positive value")
    private Integer workHours;

    private Boolean isAdmin = false;
}
