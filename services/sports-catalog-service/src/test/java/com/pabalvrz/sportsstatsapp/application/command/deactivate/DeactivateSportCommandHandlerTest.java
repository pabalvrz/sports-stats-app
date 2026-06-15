package com.pabalvrz.sportsstatsapp.application.command.deactivate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pabalvrz.sportsstatsapp.application.exception.SportNotFoundException;
import com.pabalvrz.sportsstatsapp.application.result.SportResult;
import com.pabalvrz.sportsstatsapp.domain.model.Sport;
import com.pabalvrz.sportsstatsapp.domain.ports.out.SportRepositoryPort;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class DeactivateSportCommandHandlerTest {

	private final InMemorySportRepository sportRepository = new InMemorySportRepository();
	private final DeactivateSportCommandHandler handler = new DeactivateSportCommandHandler(sportRepository);

	@Test
	void deactivatesSport() {
		UUID id = UUID.randomUUID();
		sportRepository.save(Sport.reconstitute(id, "Football", true));

		SportResult deactivatedSport = handler.handle(new DeactivateSportCommand(id));

		assertThat(deactivatedSport.id()).isEqualTo(id);
		assertThat(deactivatedSport.name()).isEqualTo("Football");
		assertThat(deactivatedSport.active()).isFalse();
		assertThat(sportRepository.findById(id)).hasValueSatisfying(sport -> assertThat(sport.isActive()).isFalse());
	}

	@Test
	void failsWhenSportDoesNotExist() {
		UUID id = UUID.randomUUID();

		assertThatThrownBy(() -> handler.handle(new DeactivateSportCommand(id)))
				.isInstanceOf(SportNotFoundException.class)
				.hasMessage("Sport not found: " + id);
	}

	@Test
	void exposesHandledCommandType() {
		assertThat(handler.commandType()).isEqualTo(DeactivateSportCommand.class);
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

		@Override
		public List<Sport> findByActive(boolean active) {
			return sports.stream().filter(sport -> sport.isActive() == active).toList();
		}
	}
}
