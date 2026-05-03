package com.zakneer.backend.repository;

import com.zakneer.backend.entity.EquipoEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipoRepository extends CrudRepository<EquipoEntity,Long> {

    @Query(value = "SELECT * FROM equipo WHERE id_tier = (" +
            "  SELECT id FROM tier WHERE tier = :tier" +
            ") ORDER BY RAND() LIMIT 1",
            nativeQuery = true)
    EquipoEntity findRandomEquipoByTierName(String tier);

    boolean existsByNombreAndLigaId(String nombre, Long idLiga);

}
