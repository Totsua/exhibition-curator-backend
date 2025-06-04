package com.example.curator.repository;

import com.example.curator.model.Artwork;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ArtworkRepository extends CrudRepository<Artwork,Long> {
    Optional<Artwork> findByApiIdAndApiOrigin(Long apiId, String apiOrigin);
    boolean existsByApiIdAndApiOrigin(Long apiId, String apiOrigin);
}
