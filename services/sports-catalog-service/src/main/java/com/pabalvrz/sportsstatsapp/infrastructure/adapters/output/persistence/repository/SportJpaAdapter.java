package com.pabalvrz.sportsstatsapp.infrastructure.adapters.output.persistence.repository;

import com.pabalvrz.sportsstatsapp.domain.model.Sport;
import com.pabalvrz.sportsstatsapp.domain.ports.out.SportRepositoryPort;
import com.pabalvrz.sportsstatsapp.infrastructure.adapters.output.persistence.mapper.SportPersistenceMapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class SportJpaAdapter implements SportRepositoryPort {

	private final SpringDataSportJpaRepository springDataRepository;

	public SportJpaAdapter(SpringDataSportJpaRepository springDataRepository) {
		this.springDataRepository = springDataRepository;
	}

	@Override
	public Sport save(Sport sport) {
		return SportPersistenceMapper.toDomain(springDataRepository.save(SportPersistenceMapper.toEntity(sport)));
	}

	@Override
	public Optional<Sport> findById(UUID id) {
		return springDataRepository.findById(id).map(SportPersistenceMapper::toDomain);
	}

	@Override
	public Optional<Sport> findByName(String name) {
		return springDataRepository.findByNameMatching(name).map(SportPersistenceMapper::toDomain);
	}

	@Override
	public List<Sport> findAll() {
		return springDataRepository.findAll().stream().map(SportPersistenceMapper::toDomain).toList();
	}

	@Override
	public List<Sport> findByActive(boolean active) {
		return springDataRepository.findByActive(active).stream().map(SportPersistenceMapper::toDomain).toList();
	}
}
