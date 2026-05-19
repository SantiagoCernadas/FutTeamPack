package com.zakneer.backend.repository;

import com.zakneer.backend.entity.AmistadEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AmistadRepository extends CrudRepository<AmistadEntity,Long> {


    @Query(value = "SELECT a FROM amistad a WHERE " +
            "(a.id_usuario_envia = :id OR a.id_usuario_recibe = :id) " +
            "AND a.estado = 'ACEPTADA'",nativeQuery = true)
    List<AmistadEntity> findAmigosUsuario(Long id);

    @Query(value = "SELECT a FROM amistad a WHERE " +
            "(a.id_usuario_envia = :id1 AND a.id_usuario_recibe = :id2) OR" +
            "(a.id_usuario_envia = :id2 AND a.id_usuario_recibe = :id1)" +
            "AND a.estado = 'PENDIENTE'",nativeQuery = true)
    Optional<AmistadEntity> findSolicitudPendiente(Long id1,Long id2);

    @Query(value = "SELECT a FROM amistad a WHERE " +
            "(a.id_usuario_envia = :id1 AND a.id_usuario_recibe = :id2)" +
            "AND a.estado = 'RECHAZADA'",nativeQuery = true)
    List<AmistadEntity> findSolicitudesRechazadas(Long id1,Long id2);
}
