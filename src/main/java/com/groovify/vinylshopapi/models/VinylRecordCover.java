package com.groovify.vinylshopapi.models;

import jakarta.persistence.*;
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

    private String filename;

    private String fileType;

    private String downloadUrl;

    @Lob
    private byte[] data;
}
