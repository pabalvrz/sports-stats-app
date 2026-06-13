package com.pabalvrz.sportsstatsapp.infrastructure.adapters.input.rest.response;

import com.pabalvrz.sportsstatsapp.domain.model.Sport;
import java.util.UUID;

public record SportResponse(UUID id, String name, boolean active) {

	public static SportResponse from(Sport sport) {
		return new SportResponse(sport.getId(), sport.getName(), sport.isActive());
	}
}
