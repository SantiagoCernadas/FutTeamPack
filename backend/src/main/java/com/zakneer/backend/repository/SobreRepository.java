package com.zakneer.backend.repository;

import com.zakneer.backend.entity.EstadoSobreId;
import com.zakneer.backend.entity.SobreEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SobreRepository extends CrudRepository<SobreEntity, EstadoSobreId> {
}
