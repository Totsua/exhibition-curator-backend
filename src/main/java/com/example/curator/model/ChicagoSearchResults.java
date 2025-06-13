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
public class ChicagoSearchResults {
    private ArrayList<ArtworkDTO> artworks;
    private Integer total_pages;
}
