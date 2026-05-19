package com.medicalstore.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medicines")
@Data
@NoArgsConstructor
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String category;

    @Column(nullable = false)
    private double price;

    private int stockQuantity;

    @Column(length = 1000)
    private String description;

    private String imageUrl;

    private boolean requiresPrescription;
}