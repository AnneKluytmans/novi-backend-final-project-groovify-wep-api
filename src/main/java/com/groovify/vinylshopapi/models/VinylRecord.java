package com.groovify.vinylshopapi.models;

import com.groovify.vinylshopapi.enums.Genre;

import com.groovify.vinylshopapi.validation.ValidDate;
import com.groovify.vinylshopapi.validation.ValidEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vinyl_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VinylRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot be longer than 100 characters")
    private String title;

    @Size(max = 500, message = "Description cannot be longer than 500 characters")
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Genre is required")
    @ValidEnum(enumClass = Genre.class, message = "Invalid genre")
    private Genre genre;

    @NotBlank(message = "Label is required")
    @Size(max = 100, message = "Label cannot be longer than 100 characters")
    private String label;

    @Column(precision = 10, scale = 2)
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Release date is required")
    @ValidDate(min = "1900-01-01", max = "now+1Y", message = "Release date must be between 1900 and a year from now")
    private LocalDate releaseDate;

    @NotNull(message = "Play time is required")
    @Positive(message = "Play time must be greater than 0 seconds")
    private Long playTimeSeconds;

    @NotNull(message = "Limited edition status is required")
    private Boolean isLimitedEdition;

    @NotNull(message = "EAN is required")
    @Pattern(regexp = "\\d{13}", message = "EAN must be a valid 13-digit number")
    private String ean;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "artist_id", referencedColumnName = "id")
    private Artist artist;

    @OneToOne(mappedBy = "vinylRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private VinylRecordStock stock;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cover_id", referencedColumnName = "id")
    private VinylRecordCover cover;

    @ManyToMany(mappedBy = "favoriteVinylRecords")
    private List<Customer> customers = new ArrayList<>();

    @OneToMany(mappedBy = "vinylRecord")
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToMany(mappedBy = "discountVinylRecords")
    private List<DiscountCode> discountCodes = new ArrayList<>();
}
