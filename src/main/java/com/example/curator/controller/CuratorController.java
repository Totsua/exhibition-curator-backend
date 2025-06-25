package com.example.curator.controller;

import com.example.curator.dto.*;
import com.example.curator.model.ArtworkResults;
import com.example.curator.service.CuratorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class CuratorController {
    @Autowired
    CuratorService service;

    @GetMapping("/random")
    public ResponseEntity<ArtworkDTO> getRandomMetArtwork(){
        return new ResponseEntity<>(service.getRandomMetArtwork(), HttpStatus.OK);
    }
    @GetMapping("/search")
    public ResponseEntity<ArtworkResults> getArtworkSearchResults(@RequestParam("query") String query, @RequestParam("page") Integer page){
    return new ResponseEntity<>(service.getArtworkSearchResults(query,page), HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<ArtworkDTO> getApiArtworkDetailsById(@RequestBody @Valid ApiArtworkIdDTO artworkDetails){
        return new ResponseEntity<>(service.getApiArtworkDetails(artworkDetails),HttpStatus.OK);
    }
    // Create an exhibition
    @PostMapping("/exhibitions/create")
    public ResponseEntity<ExhibitionDTO> createExhibition(@RequestBody @Valid ExhibitionCreateDTO exhibitionDTO){
        return new ResponseEntity<>(service.createExhibition(exhibitionDTO),HttpStatus.CREATED);
    }
    // Read all exhibitions (probably send id,title and description)
    @GetMapping("/exhibitions")
    public ResponseEntity<List<ExhibitionDTO>> getAllExhibitions(){
        return new ResponseEntity<>(service.getAllExhibitions(),HttpStatus.OK);
    }
    @GetMapping("/exhibitions/{exhibitionId}")
    public ResponseEntity<ExhibitionDTO> getExhibitionDetails(@PathVariable Long exhibitionId){
        return new ResponseEntity<>(service.getExhibitionDetails(exhibitionId),HttpStatus.OK);
    }

    // Update an exhibition
    @PatchMapping("/exhibitions/{exhibitionId}")
    public ResponseEntity<ExhibitionDTO> updateExhibitionDetails(@PathVariable Long exhibitionId, @RequestBody @Validated ExhibitionPatchDTO exhibition){
        return new ResponseEntity<>(service.updateExhibitionDetails(exhibitionId,exhibition), HttpStatus.OK);
    }
    // Delete an exhibition
    @DeleteMapping("/exhibitions/{exhibitionId}")
    public ResponseEntity<ExhibitionDTO> deleteExhibition(@PathVariable Long exhibitionId){
        service.deleteExhibition(exhibitionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Save artwork to exhibition
    @PostMapping("/exhibitions/{exhibitionId}/artworks")
    public ResponseEntity<ExhibitionDTO> saveExhibitionArt(@PathVariable Long exhibitionId, @RequestBody @Valid ApiArtworkIdDTO artworkDTO){
        return new ResponseEntity<>(service.saveExhibitionArt(exhibitionId, artworkDTO),HttpStatus.CREATED);
    }
    // Delete artwork from exhibition
    @DeleteMapping("/exhibitions/{exhibitionId}/artworks")
    public ResponseEntity<ExhibitionDTO> deleteExhibitionArt(@PathVariable Long exhibitionId, @RequestBody @Valid ApiArtworkIdDTO artworkDTO){
        return new ResponseEntity<>(service.deleteExhibitionArt(exhibitionId,artworkDTO),HttpStatus.OK);
    }

}
