package com.groovify.vinylshopapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@DiscriminatorValue("EMPLOYEE")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends User {
    @NotBlank(message = "Job title is required")
    @Size(max = 50, message = "Job title cannot be longer than 50 characters")
    private String jobTitle;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be a positive value")
    private Double salary;

    @NotNull(message = "Work hours are required")
    @Positive(message = "Work hours must be a positive value")
    private Integer workHours;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;
}
