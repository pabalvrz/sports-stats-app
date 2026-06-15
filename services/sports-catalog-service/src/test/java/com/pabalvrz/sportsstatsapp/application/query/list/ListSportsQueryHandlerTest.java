package com.pabalvrz.sportsstatsapp.application.query.list;

import static org.assertj.core.api.Assertions.assertThat;

import com.pabalvrz.sportsstatsapp.application.result.SportResult;
import com.pabalvrz.sportsstatsapp.domain.model.Sport;
import com.pabalvrz.sportsstatsapp.domain.ports.out.SportRepositoryPort;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ListSportsQueryHandlerTest {

	private final InMemorySportRepository sportRepository = new InMemorySportRepository();
	private final ListSportsQueryHandler handler = new ListSportsQueryHandler(sportRepository);

	@Test
	void listsSports() {
		Sport football = Sport.reconstitute(UUID.randomUUID(), "Football", true);
		Sport tennis = Sport.reconstitute(UUID.randomUUID(), "Tennis", false);
		sportRepository.save(football);
		sportRepository.save(tennis);

		List<SportResult> sports = handler.handle(new ListSportsQuery(null));

		assertThat(sports).containsExactly(SportResult.from(football), SportResult.from(tennis));
	}

	@Test
	void listsOnlyActiveSports() {
		Sport football = Sport.reconstitute(UUID.randomUUID(), "Football", true);
		Sport tennis = Sport.reconstitute(UUID.randomUUID(), "Tennis", false);
		sportRepository.save(football);
		sportRepository.save(tennis);

		List<SportResult> sports = handler.handle(new ListSportsQuery(true));

		assertThat(sports).containsExactly(SportResult.from(football));
	}

	@Test
	void listsOnlyInactiveSports() {
		Sport football = Sport.reconstitute(UUID.randomUUID(), "Football", true);
		Sport tennis = Sport.reconstitute(UUID.randomUUID(), "Tennis", false);
		sportRepository.save(football);
		sportRepository.save(tennis);

		List<SportResult> sports = handler.handle(new ListSportsQuery(false));

		assertThat(sports).containsExactly(SportResult.from(tennis));
	}

	@Test
	void exposesHandledQueryType() {
		assertThat(handler.queryType()).isEqualTo(ListSportsQuery.class);
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

		@Override
		public List<Sport> findByActive(boolean active) {
			return sports.stream().filter(sport -> sport.isActive() == active).toList();
		}
	}
}
