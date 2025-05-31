package com.example.curator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ArtworkDTO {
    private int id;
    private String title;
    private String description;
    private String altText;
    private String apiOrigin;
    private String imageUrl;
    private ArtistDTO artist;
}
