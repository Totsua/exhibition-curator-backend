package com.example.curator.repository;

import com.example.curator.model.Collection;
import org.springframework.data.repository.CrudRepository;

public interface CollectionRepository extends CrudRepository<Collection, Long> {
}
