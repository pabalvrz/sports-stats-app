package com.pabalvrz.sportsstatsapp.infrastructure.adapters.input.rest.exception;

import com.pabalvrz.sportsstatsapp.application.exception.SportNotFoundException;
import com.pabalvrz.sportsstatsapp.domain.exception.SportIdentifierRequiredException;
import com.pabalvrz.sportsstatsapp.domain.exception.SportNameRequiredException;
import com.pabalvrz.sportsstatsapp.infrastructure.adapters.input.rest.exception.RestErrorResponse.FieldErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

	private static final String INTERNAL_ERROR_MESSAGE = "An unexpected error occurred";
	private static final String MISSING_HANDLER_MESSAGE = "Application handler is not configured";
	private static final String REQUEST_VALIDATION_FAILED_MESSAGE = "Request validation failed";
	private static final String DUPLICATED_SPORT_NAME_MESSAGE = "Sport name already exists";
	private static final String INVALID_VALUE_MESSAGE = "Invalid value";

	@ExceptionHandler(SportNotFoundException.class)
	public ResponseEntity<RestErrorResponse> handleSportNotFound(
			SportNotFoundException exception,
			HttpServletRequest request
	) {
		return error(HttpStatus.NOT_FOUND, exception.getMessage(), request);
	}

	@ExceptionHandler({
			SportIdentifierRequiredException.class,
			SportNameRequiredException.class
	})
	public ResponseEntity<RestErrorResponse> handleDomainValidationError(
			RuntimeException exception,
			HttpServletRequest request
	) {
		return error(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<RestErrorResponse> handleMethodArgumentNotValid(
			MethodArgumentNotValidException exception,
			HttpServletRequest request
	) {
		List<FieldErrorResponse> fieldErrors = exception.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(RestExceptionHandler::toFieldErrorResponse)
				.toList();

		return validationError(fieldErrors, request);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<RestErrorResponse> handleConstraintViolation(
			ConstraintViolationException exception,
			HttpServletRequest request
	) {
		List<FieldErrorResponse> fieldErrors = exception.getConstraintViolations()
				.stream()
				.map(violation -> new FieldErrorResponse(
						violation.getPropertyPath().toString(),
						violation.getMessage()
				))
				.toList();

		return validationError(fieldErrors, request);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<RestErrorResponse> handleDataIntegrityViolation(
			DataIntegrityViolationException exception,
			HttpServletRequest request
	) {
		LOGGER.debug("Data integrity violation while handling REST request", exception);

		return error(HttpStatus.CONFLICT, DUPLICATED_SPORT_NAME_MESSAGE, request);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<RestErrorResponse> handleIllegalArgument(
			IllegalArgumentException exception,
			HttpServletRequest request
	) {
		if (isMissingHandler(exception)) {
			LOGGER.error("Missing application handler while handling REST request", exception);
			return error(HttpStatus.INTERNAL_SERVER_ERROR, MISSING_HANDLER_MESSAGE, request);
		}

		LOGGER.error("Unexpected illegal argument while handling REST request", exception);
		return error(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_ERROR_MESSAGE, request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<RestErrorResponse> handleUnexpected(
			Exception exception,
			HttpServletRequest request
	) {
		LOGGER.error("Unexpected error while handling REST request", exception);

		return error(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_ERROR_MESSAGE, request);
	}

	private static FieldErrorResponse toFieldErrorResponse(FieldError fieldError) {
		String message = fieldError.getDefaultMessage() != null
				? fieldError.getDefaultMessage()
				: INVALID_VALUE_MESSAGE;

		return new FieldErrorResponse(fieldError.getField(), message);
	}

	private static ResponseEntity<RestErrorResponse> validationError(
			List<FieldErrorResponse> fieldErrors,
			HttpServletRequest request
	) {
		HttpStatus status = HttpStatus.BAD_REQUEST;

		RestErrorResponse response = RestErrorResponse.withFieldErrors(
				status.value(),
				status.getReasonPhrase(),
				REQUEST_VALIDATION_FAILED_MESSAGE,
				request.getRequestURI(),
				fieldErrors
		);

		return ResponseEntity.status(status).body(response);
	}

	private static boolean isMissingHandler(IllegalArgumentException exception) {
		String message = exception.getMessage();

		return message != null
				&& (message.startsWith("No command handler registered for ")
				|| message.startsWith("No query handler registered for "));
	}

	private static ResponseEntity<RestErrorResponse> error(
			HttpStatus status,
			String message,
			HttpServletRequest request
	) {
		RestErrorResponse response = RestErrorResponse.of(
				status.value(),
				status.getReasonPhrase(),
				message,
				request.getRequestURI()
		);

		return ResponseEntity.status(status).body(response);
	}
}