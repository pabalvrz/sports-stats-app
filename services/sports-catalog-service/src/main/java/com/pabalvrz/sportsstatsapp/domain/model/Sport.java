package com.pabalvrz.sportsstatsapp.domain.model;

import com.pabalvrz.sportsstatsapp.domain.exception.SportIdentifierRequiredException;
import com.pabalvrz.sportsstatsapp.domain.exception.SportNameRequiredException;
import java.util.UUID;
import lombok.Getter;

@Getter
public final class Sport {

	private final UUID id;
	private final String name;
	private final boolean active;

	private Sport(UUID id, String name, boolean active) {
		if (id == null) {
			throw new SportIdentifierRequiredException();
		}

		this.id = id;
		this.name = requireName(name);
		this.active = active;
	}

	public static Sport create(String name) {
		return new Sport(UUID.randomUUID(), name, true);
	}

	public static Sport reconstitute(UUID id, String name, boolean active) {
		return new Sport(id, name, active);
	}

	public Sport rename(String name) {
		return new Sport(id, name, active);
	}

	public Sport activate() {
		return new Sport(id, name, true);
	}

	public Sport deactivate() {
		return new Sport(id, name, false);
	}

	private static String requireName(String value) {
		if (value == null || value.isBlank()) {
			throw new SportNameRequiredException();
		}

		return value.trim();
	}
}
