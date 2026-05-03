package com.zakneer.backend.repository;

import com.zakneer.backend.entity.TierEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TierRepository  extends CrudRepository<TierEntity,Long> {
    Optional<TierEntity> findByTier(String tier);
}
