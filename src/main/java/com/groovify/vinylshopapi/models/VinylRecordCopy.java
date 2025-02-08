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
    @NotNull(message = "Serial number is required")
    @Pattern(regexp = "VYN-\\d{4}-\\d{4}", message = "Serial number must follow the pattern VYN-XXXX-YYYY")
    private String serialNumber;

    @NotNull(message = "Sold status is required")
    Boolean isSold = false;

    @ManyToOne
    @JoinColumn(name = "vinyl_record_id", referencedColumnName = "id")
    private VinylRecord vinylRecord;
}
