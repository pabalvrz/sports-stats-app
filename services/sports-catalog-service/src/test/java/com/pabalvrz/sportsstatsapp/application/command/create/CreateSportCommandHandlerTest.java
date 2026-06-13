package com.pabalvrz.sportsstatsapp.application.command.create;

import static org.assertj.core.api.Assertions.assertThat;

import com.pabalvrz.sportsstatsapp.application.result.SportResult;
import com.pabalvrz.sportsstatsapp.domain.model.Sport;
import com.pabalvrz.sportsstatsapp.domain.ports.out.SportRepositoryPort;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CreateSportCommandHandlerTest {

	private final InMemorySportRepository sportRepository = new InMemorySportRepository();
	private final CreateSportCommandHandler handler = new CreateSportCommandHandler(sportRepository);

	@Test
	void createsActiveSport() {
		SportResult sport = handler.handle(new CreateSportCommand("Football"));

		assertThat(sport.id()).isNotNull();
		assertThat(sport.name()).isEqualTo("Football");
		assertThat(sport.active()).isTrue();
		assertThat(sportRepository.findAll())
				.extracting(Sport::getName)
				.containsExactly("Football");
	}

	@Test
	void exposesHandledCommandType() {
		assertThat(handler.commandType()).isEqualTo(CreateSportCommand.class);
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
