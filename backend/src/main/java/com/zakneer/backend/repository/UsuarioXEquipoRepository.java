package com.zakneer.backend.repository;

import com.zakneer.backend.entity.UsuarioEquipoId;
import com.zakneer.backend.entity.UsuarioXEquipoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioXEquipoRepository extends CrudRepository<UsuarioXEquipoEntity, UsuarioEquipoId> {
    @Query(value = "SELECT u.* FROM usuarioxequipo u " +
            "INNER JOIN equipo e ON u.id_equipo = e.id " +
            "INNER JOIN tier t ON e.id_tier = t.id " +
            "WHERE u.id_usuario = :idUsuario " +
            "ORDER BY t.valor DESC",
            countQuery = "SELECT count(*) FROM usuarioxequipo u " +
                    "INNER JOIN equipo e ON u.id_equipo = e.id " +
                    "INNER JOIN tier t ON e.id_tier = t.id " +
                    "WHERE u.id_usuario = :idUsuario",
            nativeQuery = true)
    Page<UsuarioXEquipoEntity> getUsuarioXEquipoById_usuario(Long idUsuario, Pageable pageable);
}
