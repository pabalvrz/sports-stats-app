package com.pabalvrz.sportsstatsapp.infrastructure.adapters.output.persistence.repository;

import com.pabalvrz.sportsstatsapp.infrastructure.adapters.output.persistence.entity.SportJpaEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataSportJpaRepository extends JpaRepository<SportJpaEntity, UUID> {

	Optional<SportJpaEntity> findByName(String name);
}
