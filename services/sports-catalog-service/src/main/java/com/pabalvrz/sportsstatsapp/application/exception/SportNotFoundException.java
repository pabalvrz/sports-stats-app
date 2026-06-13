package com.pabalvrz.sportsstatsapp.application.exception;

import java.util.UUID;

public class SportNotFoundException extends RuntimeException {

	public SportNotFoundException(UUID id) {
		super("Sport not found: " + id);
	}
}
