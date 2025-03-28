package com.groovify.vinylshopapi.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsResponseDTO {
    private Long userId;
    private String username;
    private Set<String> roles;
}
