package com.pabalvrz.sportsstatsapp.domain.exception;

public class SportIdentifierRequiredException extends RuntimeException {

	public SportIdentifierRequiredException() {
		super("Sport identifier is required");
	}
}
