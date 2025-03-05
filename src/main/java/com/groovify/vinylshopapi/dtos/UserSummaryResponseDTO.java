package com.groovify.vinylshopapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryResponseDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
}
