package com.groovify.vinylshopapi.models;

import com.groovify.vinylshopapi.enums.RoleType;
import com.groovify.vinylshopapi.validation.ValidEnum;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "roles")
@Data
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    @ValidEnum(enumClass = RoleType.class, message = "Invalid role type")
    private RoleType roleType;

    @Override
    public String getAuthority() {
        return roleType.getAuthority();
    }
}
