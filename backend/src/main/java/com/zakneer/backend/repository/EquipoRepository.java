package com.zakneer.backend.repository;

import com.zakneer.backend.entity.EquipoEntity;
import com.zakneer.backend.entity.UsuarioXEquipoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query(value = "SELECT e.* FROM equipo e " +
            "INNER JOIN tier t ON e.id_tier = t.id " +
            "INNER JOIN liga l ON e.id_liga = l.id " +
            "ORDER BY l.nombre,t.valor DESC",
            countQuery = "SELECT e.* FROM equipo e " +
                    "INNER JOIN tier t ON e.id_tier = t.id " +
                    "INNER JOIN liga l ON e.id_liga = l.id ",
            nativeQuery = true)
    Page<EquipoEntity> getEquipos(Pageable pageable);

}
