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
// Add a unique constraint to apiOrigin and apiId so only one "apiId" and "apiOrigin" combination can be on the db
@Table(name = "artwork", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"apiId", "apiOrigin"})
})
public class Artwork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long apiId;

    private String title;
    @Lob
    private String description;
    @Lob
    private String altText;

    @JoinColumn(name = "artist_id")
    @ManyToOne
    private Artist artist;

    private String imageUrl;
    private String apiOrigin;

}
