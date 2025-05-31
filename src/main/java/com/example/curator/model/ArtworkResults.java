package com.example.curator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ArtworkResults {
    private String query;
    private Integer page;
    private ArrayList<Artwork> artworks;
}
