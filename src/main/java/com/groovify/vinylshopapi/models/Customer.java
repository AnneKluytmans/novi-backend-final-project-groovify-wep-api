package com.groovify.vinylshopapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("CUSTOMER")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Customer extends User {
    @NotNull(message = "Subscribe status is required")
    private Boolean newsletterSubscribed = true;

    @ManyToMany
    @JoinTable(
            name = "customer_favorite_vinyl_records",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "vinyl_record_id")
    )
    private List<VinylRecord> favoriteVinylRecords = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    public Customer(String username, String email, String password, String firstName, String lastName,
                    LocalDate dateOfBirth, String phone, Boolean newsletterSubscribed) {
        super(username, email, password, firstName, lastName, dateOfBirth, phone);
        this.newsletterSubscribed = newsletterSubscribed;
    }
}
