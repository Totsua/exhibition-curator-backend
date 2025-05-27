package com.example.curator.service;

import com.example.curator.model.Artwork;

import java.util.List;

public interface CuratorService {
    List<Artwork> getArtworkSearchResults(String query);
}
