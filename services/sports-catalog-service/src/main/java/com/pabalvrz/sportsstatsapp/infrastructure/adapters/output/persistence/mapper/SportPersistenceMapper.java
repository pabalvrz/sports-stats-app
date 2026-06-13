package com.pabalvrz.sportsstatsapp.infrastructure.adapters.output.persistence.mapper;

import com.pabalvrz.sportsstatsapp.domain.model.Sport;
import com.pabalvrz.sportsstatsapp.infrastructure.adapters.output.persistence.entity.SportJpaEntity;

public final class SportPersistenceMapper {

	private SportPersistenceMapper() {
	}

	public static SportJpaEntity toEntity(Sport sport) {
		return new SportJpaEntity(sport.getId(), sport.getName(), sport.isActive());
	}

	public static Sport toDomain(SportJpaEntity entity) {
		return Sport.reconstitute(entity.getId(), entity.getName(), entity.isActive());
	}
}
