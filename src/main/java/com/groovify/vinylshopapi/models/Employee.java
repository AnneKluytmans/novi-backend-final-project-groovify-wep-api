package com.groovify.vinylshopapi.models;

import com.groovify.vinylshopapi.validation.ValidDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("EMPLOYEE")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends User {
    @NotNull(message = "Job title is required")
    @Size(min = 3, max = 50, message = "Job title must be between 3 and 50 characters")
    private String jobTitle;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be a positive value")
    private Double salary;

    @NotNull(message = "Start date is required")
    @ValidDate(min = "1990-01-01", mustBePast = true, message = "Start date cannot be in the future")
    private LocalDate startDate;
}
