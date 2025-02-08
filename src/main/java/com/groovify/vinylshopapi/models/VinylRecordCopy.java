package com.groovify.vinylshopapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vinyl_record_copies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VinylRecordCopy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull(message = "Product number is required")
    @Pattern(regexp = "VN-\\d{4}-\\d{4}", message = "Product number must follow the pattern VN-YYYY-XXXX")
    private String productNumber;

    @NotNull(message = "Sold status is required")
    Boolean isSold = false;

    @ManyToOne
    @JoinColumn(name = "vinyl_record_id", referencedColumnName = "id")
    private VinylRecord vinylRecord;
}
