package com.pabalvrz.sportsstatsapp.application.result;

import com.pabalvrz.sportsstatsapp.domain.model.Sport;
import java.util.UUID;

public record SportResult(UUID id, String name, boolean active) {

	public static SportResult from(Sport sport) {
		return new SportResult(sport.getId(), sport.getName(), sport.isActive());
	}
}
