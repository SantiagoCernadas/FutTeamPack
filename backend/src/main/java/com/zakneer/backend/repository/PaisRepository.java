package com.zakneer.backend.repository;

import com.zakneer.backend.entity.PaisEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaisRepository extends CrudRepository<PaisEntity,Long> {
    Optional<PaisEntity> findByNombre(String nombre);
}
