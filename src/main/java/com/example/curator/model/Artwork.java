package com.example.curator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Artwork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String description;
    private String altText;

    @JoinColumn(name = "artist_id")
    @ManyToOne
    private Artist artist;

    private String imageUrl;
    private String apiOrigin;

}
