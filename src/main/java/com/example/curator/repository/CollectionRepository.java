package com.example.curator.repository;

import com.example.curator.model.Exhibition;
import org.springframework.data.repository.CrudRepository;

public interface CollectionRepository extends CrudRepository<Exhibition, Long> {
}
