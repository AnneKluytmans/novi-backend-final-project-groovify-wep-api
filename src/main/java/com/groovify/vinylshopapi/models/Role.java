package com.groovify.vinylshopapi.models;

import com.groovify.vinylshopapi.enums.RoleType;
import com.groovify.vinylshopapi.validation.ValidEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    @ValidEnum(enumClass = RoleType.class, message = "Invalid role type")
    private RoleType roleType;
}
