package com.groovify.vinylshopapi.models;

import com.groovify.vinylshopapi.validation.ValidDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ValidDate(min = "2025-01-01", mustBePast = true, message = "Debut date must be between 2025 and now")
    private LocalDateTime createdAt;

    @ValidDate(min = "2025-01-01", mustBePast = true, message = "Debut date must be between 2025 and now")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

}
