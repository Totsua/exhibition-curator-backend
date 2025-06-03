package com.example.curator.service;

import com.example.curator.dto.ArtworkDTO;
import com.example.curator.dto.ExhibitionDTO;
import com.example.curator.model.ArtworkResults;

import java.util.List;

public interface CuratorService {
    ArtworkResults getArtworkSearchResults(String query, Integer page);
    ArtworkDTO getApiArtworkDetails(Long id, String apiOrigin);
    ExhibitionDTO createExhibition(String title);
    List<ExhibitionDTO> getAllExhibitions();
}
