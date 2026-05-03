package com.zakneer.backend.repository;

import com.zakneer.backend.entity.LigaEntity;
import com.zakneer.backend.entity.UsuarioEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LigaRepository extends CrudRepository<LigaEntity,Long> {
    Optional<LigaEntity> findByNombre(String nombre);
}
