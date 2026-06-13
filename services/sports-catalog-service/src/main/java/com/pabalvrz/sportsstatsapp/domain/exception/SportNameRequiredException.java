package com.pabalvrz.sportsstatsapp.domain.exception;

public class SportNameRequiredException extends RuntimeException {

	public SportNameRequiredException() {
		super("Sport name is required");
	}
}
