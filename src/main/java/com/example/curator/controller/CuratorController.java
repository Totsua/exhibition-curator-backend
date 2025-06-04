package com.example.curator.controller;

import com.example.curator.dto.ArtworkDTO;
import com.example.curator.dto.ExhibitionDTO;
import com.example.curator.model.ArtworkResults;
import com.example.curator.service.CuratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class CuratorController {
    @Autowired
    CuratorService service;

    @GetMapping("/search")
    public ResponseEntity<ArtworkResults> getArtworkSearchResults(@RequestParam("query") String query, @RequestParam("page") Integer page){
    return new ResponseEntity<>(service.getArtworkSearchResults(query,page), HttpStatus.OK);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<ArtworkDTO> getApiArtworkDetailsById(@RequestBody ApiArtworkIdDTO artworkDetails){
        return new ResponseEntity<>(service.getApiArtworkDetails(artworkDetails),HttpStatus.OK);
    }
    // Create an exhibition
    @PostMapping("/exhibitions/create")
    public ResponseEntity<ExhibitionDTO> createExhibition(@RequestParam String title){
        return new ResponseEntity<>(service.createExhibition(title),HttpStatus.CREATED);
    }
    // Read all exhibitions (probably send id,title and description)
    @GetMapping("/exhibitions")
    public ResponseEntity<List<ExhibitionDTO>> getAllExhibitions(){
        return new ResponseEntity<>(service.getAllExhibitions(),HttpStatus.OK);
    }
    // Update an exhibition
    @PatchMapping("/exhibitions/{id}")
    public ResponseEntity<ExhibitionDTO> updateExhibitionDetails(@PathVariable Long id, @RequestBody ExhibitionDTO exhibition){
        return new ResponseEntity<>(service.updateExhibitionDetails(id,exhibition), HttpStatus.OK);
    }
    // Delete an exhibition
    @DeleteMapping("/exhibitions/{id}")
    public ResponseEntity<ExhibitionDTO> deleteExhibition(@PathVariable Long id){
        service.deleteExhibition(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
