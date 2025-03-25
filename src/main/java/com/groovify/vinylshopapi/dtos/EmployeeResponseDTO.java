package com.groovify.vinylshopapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EmployeeResponseDTO extends UserResponseDTO {
    private String jobTitle;
    private BigDecimal salary;
    private Integer workHours;
    private AddressResponseDTO address;
}
