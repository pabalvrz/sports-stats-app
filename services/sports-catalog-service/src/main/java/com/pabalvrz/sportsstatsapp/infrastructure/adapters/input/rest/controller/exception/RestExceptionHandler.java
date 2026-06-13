package com.pabalvrz.sportsstatsapp.infrastructure.adapters.input.rest.controller.exception;

import com.pabalvrz.sportsstatsapp.application.exception.SportNotFoundException;
import com.pabalvrz.sportsstatsapp.domain.exception.SportIdentifierRequiredException;
import com.pabalvrz.sportsstatsapp.domain.exception.SportNameRequiredException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(SportNotFoundException.class)
	public ProblemDetail handleNotFound(SportNotFoundException exception) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(
				HttpStatus.NOT_FOUND,
				exception.getMessage()
		);
		problem.setTitle("Sport not found");
		return problem;
	}

	@ExceptionHandler({
			SportIdentifierRequiredException.class,
			SportNameRequiredException.class
	})
	public ProblemDetail handleInvalidSportData(RuntimeException exception) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(
				HttpStatus.BAD_REQUEST,
				exception.getMessage()
		);
		problem.setTitle("Invalid sport data");
		return problem;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleValidationError(MethodArgumentNotValidException exception) {
		List<String> errors = exception.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage())
				.toList();

		ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		problem.setTitle("Invalid request body");
		problem.setDetail(String.join(", ", errors));
		return problem;
	}
}
