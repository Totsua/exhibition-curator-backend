package com.example.curator.service;

import com.example.curator.dto.ArtworkDTO;
import com.example.curator.dto.ExhibitionDTO;
import com.example.curator.dto.ApiArtworkIdDTO;
import com.example.curator.dto.ExhibitionPatchDTO;
import com.example.curator.model.ArtworkResults;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;

public interface CuratorService {
    ArtworkResults getArtworkSearchResults(String query, Integer page);
    ArtworkDTO getApiArtworkDetails(@Valid ApiArtworkIdDTO apiArtworkIdDTO);
    ExhibitionDTO createExhibition(String title);
    List<ExhibitionDTO> getAllExhibitions();
    ExhibitionDTO updateExhibitionDetails(Long id, @Validated ExhibitionPatchDTO exhibitionDTO);
    void deleteExhibition(Long id);
    ExhibitionDTO saveExhibitionArt(Long id, @Valid ApiArtworkIdDTO artworkDTO);
    ExhibitionDTO deleteExhibitionArt(Long exhibitionId, @Valid ApiArtworkIdDTO artworkDTO);
}
