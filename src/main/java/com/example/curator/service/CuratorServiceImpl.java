package com.example.curator.service;

import com.example.curator.dto.ArtworkDTO;
import com.example.curator.model.ArtworkResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CuratorServiceImpl implements CuratorService{
    @Autowired
    ApiService apiService;

    @Override
    public ArtworkResults getArtworkSearchResults(String query, Integer page) {
        ArtworkResults artworkSearchResults = apiService.getArtworkSearchResults(query,page);
        return artworkSearchResults;
    }

    @Override
    public ArtworkDTO getApiArtworkDetails(Long id, String apiOrigin) {
        ArtworkDTO artwork = apiService.getApiArtworkDetails(id, apiOrigin);
        // todo: catch custom artwork exception
        return artwork;
    }
}
