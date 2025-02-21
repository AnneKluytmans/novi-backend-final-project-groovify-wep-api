package com.groovify.vinylshopapi.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmployeeResponseDTO extends UserResponseDTO {
    private String jobTitle;
    private Double salary;
    private Integer workHours;
}
