package com.pabalvrz.sportsstatsapp.infrastructure.adapters.input.rest.response;

import com.pabalvrz.sportsstatsapp.application.result.SportResult;
import java.util.UUID;

public record SportResponse(UUID id, String name, boolean active) {

	public static SportResponse from(SportResult sport) {
		return new SportResponse(sport.id(), sport.name(), sport.active());
	}
}
