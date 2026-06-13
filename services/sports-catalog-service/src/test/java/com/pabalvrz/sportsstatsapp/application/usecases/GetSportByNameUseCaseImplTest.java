package com.pabalvrz.sportsstatsapp.application.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pabalvrz.sportsstatsapp.application.exception.SportNotFoundException;
import com.pabalvrz.sportsstatsapp.domain.model.Sport;
import com.pabalvrz.sportsstatsapp.domain.ports.out.SportRepositoryPort;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class GetSportByNameUseCaseImplTest {

	private final InMemorySportRepository sportRepository = new InMemorySportRepository();
	private final GetSportByNameUseCaseImpl getSportByNameUseCase = new GetSportByNameUseCaseImpl(sportRepository);

	@Test
	void getsSportByName() {
		Sport sport = Sport.reconstitute(UUID.randomUUID(), "Football", true);
		sportRepository.save(sport);

		Sport foundSport = getSportByNameUseCase.execute(" Football ");

		assertThat(foundSport).isEqualTo(sport);
	}

	@Test
	void failsWhenSportDoesNotExist() {
		assertThatThrownBy(() -> getSportByNameUseCase.execute("Football"))
				.isInstanceOf(SportNotFoundException.class)
				.hasMessage("Sport not found: Football");
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
		public Optional<Sport> findByName(String name) {
			return sports.stream().filter(sport -> sport.getName().equals(name)).findFirst();
		}

		@Override
		public List<Sport> findAll() {
			return List.copyOf(sports);
		}
	}
}
