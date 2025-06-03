package com.example.curator.controller;

import com.example.curator.dto.ArtworkDTO;
import com.example.curator.model.ArtworkResults;
import com.example.curator.service.CuratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class CuratorController {
    @Autowired
    CuratorService service;

    @GetMapping("/search")
    public ResponseEntity<ArtworkResults> getArtworkSearchResults(@RequestParam("query") String query, @RequestParam("page") Integer page){
    return new ResponseEntity<>(service.getArtworkSearchResults(query,page), HttpStatus.OK);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<ArtworkDTO> getApiArtworkDetailsById(@PathVariable("id") Long id,@RequestParam("apiOrigin") String apiOrigin){
        return new ResponseEntity<>(service.getApiArtworkDetails(id, apiOrigin),HttpStatus.OK);
    }

}
