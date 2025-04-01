package com.groovify.vinylshopapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


@Entity
@DiscriminatorValue("EMPLOYEE")
@EqualsAndHashCode(callSuper = true)
@Data
public class Employee extends User {
    @NotBlank(message = "Job title is required")
    @Size(max = 50, message = "Job title cannot be longer than 50 characters")
    private String jobTitle;

    @Column(precision = 10, scale = 2)
    @NotNull(message = "Salary is required")
    @DecimalMin(value = "0.00", message = "Salary must be positive")
    private BigDecimal salary;

    @NotNull(message = "Work hours are required")
    @Positive(message = "Work hours must be a positive value")
    private Integer workHours;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;
}
