package com.example.curator.service;

import com.example.curator.model.ArtworkResults;

public interface CuratorService {
    ArtworkResults getArtworkSearchResults(String query, Integer page);
}
