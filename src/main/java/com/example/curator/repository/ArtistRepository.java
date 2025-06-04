package com.example.curator.repository;

import com.example.curator.model.Artist;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ArtistRepository extends CrudRepository<Artist, Long> {
    Optional<Artist> findByApiIdAndApiOrigin(Long apiId, String apiOrigin);
    boolean existsByApiIdAndApiOrigin(Long apiId, String apiOrigin);
}
