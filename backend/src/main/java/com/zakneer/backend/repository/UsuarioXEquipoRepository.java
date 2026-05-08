package com.zakneer.backend.repository;

import com.zakneer.backend.entity.UsuarioEquipoId;
import com.zakneer.backend.entity.UsuarioXEquipoEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioXEquipoRepository extends CrudRepository<UsuarioXEquipoEntity, UsuarioEquipoId> {
    @Query(value = "SELECT * FROM usuarioxequipo where id_usuario = :idUsuario limit 5",nativeQuery = true)
    List<UsuarioXEquipoEntity> getUsuarioXEquipoById_usuario(Long idUsuario);
}
