package com.example.curator.model;

import com.example.curator.dto.ArtworkDTO;
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
    private ArrayList<ArtworkDTO> artworks;
}
