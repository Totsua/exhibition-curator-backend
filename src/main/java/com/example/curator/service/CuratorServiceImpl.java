package com.example.curator.service;

import com.example.curator.model.Artwork;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CuratorServiceImpl implements CuratorService{
    @Autowired
    ApiService apiService;

    @Override
    public List<Artwork> getArtworkSearchResults(String query) {
        return null;
    }
}
