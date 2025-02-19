package com.groovify.vinylshopapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@DiscriminatorValue("CUSTOMER")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends User {
    @ManyToMany
    @JoinTable(
            name = "favorite_vinyl_records",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "vinyl_record_id")
    )
    private Set<VinylRecord> favoriteVinylRecords;
}
