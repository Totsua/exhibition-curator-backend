package com.example.curator.service;


import com.example.curator.dto.ArtworkDTO;
import com.example.curator.model.ArtworkResults;

public interface ApiService {
    ArtworkResults getArtworkSearchResults(String query, Integer page);
    ArtworkDTO getRandomMetArtwork();
    ArtworkDTO getApiArtworkDetails(Long id, String apiOrigin);
}
