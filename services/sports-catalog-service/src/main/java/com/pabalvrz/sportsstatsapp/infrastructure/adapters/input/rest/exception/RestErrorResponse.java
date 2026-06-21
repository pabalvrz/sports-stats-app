package com.pabalvrz.sportsstatsapp.infrastructure.adapters.input.rest.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.List;

public record RestErrorResponse(
		Instant timestamp,
		int status,
		String error,
		String message,
		String path,
		@JsonInclude(JsonInclude.Include.NON_EMPTY)
		List<FieldErrorResponse> fieldErrors
) {

	public static RestErrorResponse of(int status, String error, String message, String path) {
		return new RestErrorResponse(Instant.now(), status, error, message, path, List.of());
	}

	public static RestErrorResponse withFieldErrors(
			int status,
			String error,
			String message,
			String path,
			List<FieldErrorResponse> fieldErrors
	) {
		return new RestErrorResponse(Instant.now(), status, error, message, path, fieldErrors);
	}

	public record FieldErrorResponse(String field, String message) {
	}
}
