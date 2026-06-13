package com.pabalvrz.sportsstatsapp.infrastructure.adapters.input.rest.controller;

import com.pabalvrz.sportsstatsapp.domain.model.Sport;
import com.pabalvrz.sportsstatsapp.domain.ports.in.CreateSportUseCase;
import com.pabalvrz.sportsstatsapp.domain.ports.in.GetSportByIdUseCase;
import com.pabalvrz.sportsstatsapp.domain.ports.in.GetSportByNameUseCase;
import com.pabalvrz.sportsstatsapp.domain.ports.in.ListSportsUseCase;
import com.pabalvrz.sportsstatsapp.domain.ports.in.UpdateSportUseCase;
import com.pabalvrz.sportsstatsapp.infrastructure.adapters.input.rest.request.CreateSportRequest;
import com.pabalvrz.sportsstatsapp.infrastructure.adapters.input.rest.request.UpdateSportRequest;
import com.pabalvrz.sportsstatsapp.infrastructure.adapters.input.rest.response.SportResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/sports")
public class SportController {

	private final CreateSportUseCase createSportUseCase;
	private final GetSportByIdUseCase getSportByIdUseCase;
	private final GetSportByNameUseCase getSportByNameUseCase;
	private final ListSportsUseCase listSportsUseCase;
	private final UpdateSportUseCase updateSportUseCase;

	public SportController(
			CreateSportUseCase createSportUseCase,
			GetSportByIdUseCase getSportByIdUseCase,
			GetSportByNameUseCase getSportByNameUseCase,
			ListSportsUseCase listSportsUseCase,
			UpdateSportUseCase updateSportUseCase
	) {
		this.createSportUseCase = createSportUseCase;
		this.getSportByIdUseCase = getSportByIdUseCase;
		this.getSportByNameUseCase = getSportByNameUseCase;
		this.listSportsUseCase = listSportsUseCase;
		this.updateSportUseCase = updateSportUseCase;
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

	@GetMapping("/by-name/{name}")
	public SportResponse getByName(@PathVariable String name) {
		Sport sport = getSportByNameUseCase.execute(name);

		return SportResponse.from(sport);
	}

	@PutMapping("/{id}")
	public SportResponse update(@PathVariable UUID id, @Valid @RequestBody UpdateSportRequest request) {
		Sport sport = updateSportUseCase.execute(id, request.name());

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
