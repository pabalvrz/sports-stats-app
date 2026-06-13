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

class UpdateSportUseCaseImplTest {

	private final InMemorySportRepository sportRepository = new InMemorySportRepository();
	private final UpdateSportUseCaseImpl updateSportUseCase = new UpdateSportUseCaseImpl(sportRepository);

	@Test
	void updatesSportName() {
		UUID id = UUID.randomUUID();
		sportRepository.save(Sport.reconstitute(id, "Football", false));

		Sport updatedSport = updateSportUseCase.execute(id, "Tennis");

		assertThat(updatedSport.getId()).isEqualTo(id);
		assertThat(updatedSport.getName()).isEqualTo("Tennis");
		assertThat(updatedSport.isActive()).isFalse();
		assertThat(sportRepository.findAll()).containsExactly(updatedSport);
	}

	@Test
	void failsWhenSportDoesNotExist() {
		UUID id = UUID.randomUUID();

		assertThatThrownBy(() -> updateSportUseCase.execute(id, "Tennis"))
				.isInstanceOf(SportNotFoundException.class)
				.hasMessage("Sport not found: " + id);
	}

	private static final class InMemorySportRepository implements SportRepositoryPort {

		private final List<Sport> sports = new ArrayList<>();

		@Override
		public Sport save(Sport sport) {
			sports.removeIf(existingSport -> existingSport.getId().equals(sport.getId()));
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
