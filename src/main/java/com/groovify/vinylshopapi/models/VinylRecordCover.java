package com.groovify.vinylshopapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vinyl_records_covers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VinylRecordCover {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "File name cannot be blank.")
    @Size(max = 250, message = "File name may not exceed 250 characters")
    private String filename;

    @NotBlank(message = "File type cannot be blank")
    @Size(max = 100, message = "File type may not exceed 100 characters")
    private String fileType;

    @NotBlank(message = "Download url cannot be blank")
    private String downloadUrl;

    @Lob
    private byte[] data;
}
