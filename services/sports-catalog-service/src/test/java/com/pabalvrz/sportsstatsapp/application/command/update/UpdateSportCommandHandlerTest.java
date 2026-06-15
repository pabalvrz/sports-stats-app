package com.pabalvrz.sportsstatsapp.application.command.update;

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

class UpdateSportCommandHandlerTest {

	private final InMemorySportRepository sportRepository = new InMemorySportRepository();
	private final UpdateSportCommandHandler handler = new UpdateSportCommandHandler(sportRepository);

	@Test
	void updatesSportName() {
		UUID id = UUID.randomUUID();
		sportRepository.save(Sport.reconstitute(id, "Football", false));

		SportResult updatedSport = handler.handle(new UpdateSportCommand(id, "Tennis"));

		assertThat(updatedSport.id()).isEqualTo(id);
		assertThat(updatedSport.name()).isEqualTo("Tennis");
		assertThat(updatedSport.active()).isFalse();
		assertThat(sportRepository.findAll())
				.extracting(Sport::getName)
				.containsExactly("Tennis");
	}

	@Test
	void failsWhenSportDoesNotExist() {
		UUID id = UUID.randomUUID();

		assertThatThrownBy(() -> handler.handle(new UpdateSportCommand(id, "Tennis")))
				.isInstanceOf(SportNotFoundException.class)
				.hasMessage("Sport not found: " + id);
	}

	@Test
	void exposesHandledCommandType() {
		assertThat(handler.commandType()).isEqualTo(UpdateSportCommand.class);
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
