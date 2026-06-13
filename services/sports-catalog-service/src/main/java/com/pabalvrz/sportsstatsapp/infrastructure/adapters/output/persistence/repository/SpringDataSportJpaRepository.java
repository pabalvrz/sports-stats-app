package com.pabalvrz.sportsstatsapp.infrastructure.adapters.output.persistence.repository;

import com.pabalvrz.sportsstatsapp.infrastructure.adapters.output.persistence.entity.SportJpaEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataSportJpaRepository extends JpaRepository<SportJpaEntity, UUID> {

	@Query("select sport from SportJpaEntity sport where lower(trim(sport.name)) = lower(trim(:name))")
	Optional<SportJpaEntity> findByNameMatching(@Param("name") String name);
}
