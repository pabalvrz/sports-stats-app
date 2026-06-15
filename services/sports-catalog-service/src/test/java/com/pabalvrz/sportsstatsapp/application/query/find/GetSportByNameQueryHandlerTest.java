package com.pabalvrz.sportsstatsapp.application.query.find;

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

class GetSportByNameQueryHandlerTest {

	private final InMemorySportRepository sportRepository = new InMemorySportRepository();
	private final GetSportByNameQueryHandler handler = new GetSportByNameQueryHandler(sportRepository);

	@Test
	void getsSportByName() {
		Sport sport = Sport.reconstitute(UUID.randomUUID(), "Football", true);
		sportRepository.save(sport);

		SportResult foundSport = handler.handle(new GetSportByNameQuery(" football "));

		assertThat(foundSport).isEqualTo(SportResult.from(sport));
	}

	@Test
	void failsWhenSportDoesNotExist() {
		assertThatThrownBy(() -> handler.handle(new GetSportByNameQuery("Football")))
				.isInstanceOf(SportNotFoundException.class)
				.hasMessage("Sport not found: Football");
	}

	@Test
	void exposesHandledQueryType() {
		assertThat(handler.queryType()).isEqualTo(GetSportByNameQuery.class);
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
			return sports.stream()
					.filter(sport -> sport.getName().trim().equalsIgnoreCase(name.trim()))
					.findFirst();
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
