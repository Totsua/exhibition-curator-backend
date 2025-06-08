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
public class Exhibition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private AppUser appUser;

    @Column(unique = true)
    private String title;

    @Column(length = 500)
    private String description;

    @ManyToMany
    @JoinTable(
            name = "exhibition_art",
            joinColumns = {@JoinColumn(name = "exhibition_id")},
            inverseJoinColumns = {@JoinColumn(name = "artwork_id")}
    )
    private List<Artwork> artworks;
}
