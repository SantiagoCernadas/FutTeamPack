package com.zakneer.backend.repository;

import com.zakneer.backend.entity.UsuarioEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends CrudRepository<UsuarioEntity,Long> {
    Optional<UsuarioEntity> findByNickname(String nickname);
}
