package com.pabalvrz.sportsstatsapp.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pabalvrz.sportsstatsapp.domain.exception.SportIdentifierRequiredException;
import com.pabalvrz.sportsstatsapp.domain.exception.SportNameRequiredException;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class SportTest {

	@Test
	void createsSportWithRequiredData() {
		Sport sport = Sport.create(" Football ");

		assertThat(sport.getId()).isNotNull();
		assertThat(sport.getName()).isEqualTo("Football");
		assertThat(sport.isActive()).isTrue();
	}

	@Test
	void rejectsBlankName() {
		assertThatThrownBy(() -> Sport.create(" "))
				.isInstanceOf(SportNameRequiredException.class)
				.hasMessage("Sport name is required");
	}

	@Test
	void renamesSportKeepingIdentityAndActiveState() {
		Sport sport = Sport.reconstitute(UUID.randomUUID(), "Football", false);

		Sport renamedSport = sport.rename(" Tennis ");

		assertThat(renamedSport.getId()).isEqualTo(sport.getId());
		assertThat(renamedSport.getName()).isEqualTo("Tennis");
		assertThat(renamedSport.isActive()).isFalse();
	}

	@Test
	void rejectsNullIdentifierWhenReconstituting() {
		assertThatThrownBy(() -> Sport.reconstitute(null, "Football", true))
				.isInstanceOf(SportIdentifierRequiredException.class)
				.hasMessage("Sport identifier is required");
	}
}
