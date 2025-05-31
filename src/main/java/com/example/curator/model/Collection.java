package com.example.curator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser AppUser;

    private String title;
    private String description;

    @ManyToMany
    @JoinTable(
            name = "collection_art",
            joinColumns = {@JoinColumn(name = "collection_id")},
            inverseJoinColumns = {@JoinColumn(name = "artwork_id")}
    )
    private List<Artwork> artworks;
}
