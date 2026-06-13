package com.pabalvrz.sportsstatsapp.application.usecases;

import static org.assertj.core.api.Assertions.assertThat;

import com.pabalvrz.sportsstatsapp.domain.model.Sport;
import com.pabalvrz.sportsstatsapp.domain.ports.out.SportRepositoryPort;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CreateSportUseCaseImplTest {

	private final InMemorySportRepository sportRepository = new InMemorySportRepository();
	private final CreateSportUseCaseImpl createSportUseCase = new CreateSportUseCaseImpl(sportRepository);

	@Test
	void createsActiveSport() {
		Sport sport = createSportUseCase.execute("Football");

		assertThat(sport.getId()).isNotNull();
		assertThat(sport.getName()).isEqualTo("Football");
		assertThat(sport.isActive()).isTrue();
		assertThat(sportRepository.findAll()).containsExactly(sport);
	}

	private static final class InMemorySportRepository implements SportRepositoryPort {

		private final List<Sport> sports = new ArrayList<>();

		@Override
		public Sport save(Sport sport) {
			sports.add(sport);
			return sport;
		}

		@Override
		public Optional<Sport> findById(UUID id) {
			return sports.stream().filter(sport -> sport.getId().equals(id)).findFirst();
		}

		@Override
		public List<Sport> findAll() {
			return List.copyOf(sports);
		}
	}
}
