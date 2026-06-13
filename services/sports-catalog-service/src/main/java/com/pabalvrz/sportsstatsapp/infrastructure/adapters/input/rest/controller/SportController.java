package com.pabalvrz.sportsstatsapp.infrastructure.adapters.input.rest.controller;

import com.pabalvrz.sportsstatsapp.domain.model.Sport;
import com.pabalvrz.sportsstatsapp.domain.ports.in.CreateSportUseCase;
import com.pabalvrz.sportsstatsapp.domain.ports.in.GetSportByIdUseCase;
import com.pabalvrz.sportsstatsapp.domain.ports.in.ListSportsUseCase;
import com.pabalvrz.sportsstatsapp.infrastructure.adapters.input.rest.request.CreateSportRequest;
import com.pabalvrz.sportsstatsapp.infrastructure.adapters.input.rest.response.SportResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/sports")
public class SportController {

	private final CreateSportUseCase createSportUseCase;
	private final GetSportByIdUseCase getSportByIdUseCase;
	private final ListSportsUseCase listSportsUseCase;

	public SportController(
			CreateSportUseCase createSportUseCase,
			GetSportByIdUseCase getSportByIdUseCase,
			ListSportsUseCase listSportsUseCase
	) {
		this.createSportUseCase = createSportUseCase;
		this.getSportByIdUseCase = getSportByIdUseCase;
		this.listSportsUseCase = listSportsUseCase;
	}

	@PostMapping
	public ResponseEntity<SportResponse> create(@Valid @RequestBody CreateSportRequest request) {
		Sport sport = createSportUseCase.execute(request.name());

		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(sport.getId())
				.toUri();

		return ResponseEntity.created(location)
				.body(SportResponse.from(sport));
	}

	@GetMapping("/{id}")
	public SportResponse getById(@PathVariable UUID id) {
		Sport sport = getSportByIdUseCase.execute(id);

		return SportResponse.from(sport);
	}

	@GetMapping
	public List<SportResponse> list() {
		return listSportsUseCase.execute()
				.stream()
				.map(SportResponse::from)
				.toList();
	}
}