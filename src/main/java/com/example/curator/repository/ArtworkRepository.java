package com.example.curator.repository;

import com.example.curator.model.Artwork;
import org.springframework.data.repository.CrudRepository;

public interface ArtworkRepository extends CrudRepository<Artwork,Long> {
}
