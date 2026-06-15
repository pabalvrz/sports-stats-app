package com.pabalvrz.sportsstatsapp.infrastructure.adapters.input.rest.controller;

import com.pabalvrz.sportsstatsapp.application.command.CommandBus;
import com.pabalvrz.sportsstatsapp.application.command.activate.ActivateSportCommand;
import com.pabalvrz.sportsstatsapp.application.command.create.CreateSportCommand;
import com.pabalvrz.sportsstatsapp.application.command.deactivate.DeactivateSportCommand;
import com.pabalvrz.sportsstatsapp.application.command.update.UpdateSportCommand;
import com.pabalvrz.sportsstatsapp.application.query.QueryBus;
import com.pabalvrz.sportsstatsapp.application.query.find.GetSportByIdQuery;
import com.pabalvrz.sportsstatsapp.application.query.find.GetSportByNameQuery;
import com.pabalvrz.sportsstatsapp.application.query.list.ListSportsQuery;
import com.pabalvrz.sportsstatsapp.application.result.SportResult;
import com.pabalvrz.sportsstatsapp.infrastructure.adapters.input.rest.request.CreateSportRequest;
import com.pabalvrz.sportsstatsapp.infrastructure.adapters.input.rest.request.UpdateSportRequest;
import com.pabalvrz.sportsstatsapp.infrastructure.adapters.input.rest.response.SportResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/sports")
public class SportController {

	private final CommandBus commandBus;
	private final QueryBus queryBus;

	public SportController(CommandBus commandBus, QueryBus queryBus) {
		this.commandBus = commandBus;
		this.queryBus = queryBus;
	}

	@PostMapping
	public ResponseEntity<SportResponse> create(@Valid @RequestBody CreateSportRequest request) {
		SportResult sport = commandBus.dispatch(new CreateSportCommand(request.name()));

		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(sport.id())
				.toUri();

		return ResponseEntity.created(location)
				.body(SportResponse.from(sport));
	}

	@GetMapping("/{id}")
	public ResponseEntity<SportResponse> getById(@PathVariable UUID id) {
		SportResult sport = queryBus.ask(new GetSportByIdQuery(id));

		return ResponseEntity.ok(SportResponse.from(sport));
	}

	@GetMapping("/by-name/{name}")
	public ResponseEntity<SportResponse> getByName(@PathVariable String name) {
		SportResult sport = queryBus.ask(new GetSportByNameQuery(name));

		return ResponseEntity.ok(SportResponse.from(sport));
	}

	@PutMapping("/{id}")
	public ResponseEntity<SportResponse> update(@PathVariable UUID id, @Valid @RequestBody UpdateSportRequest request) {
		SportResult sport = commandBus.dispatch(new UpdateSportCommand(id, request.name()));

		return ResponseEntity.ok(SportResponse.from(sport));
	}

	@PatchMapping("/{id}/activate")
	public ResponseEntity<SportResponse> activate(@PathVariable UUID id) {
		SportResult sport = commandBus.dispatch(new ActivateSportCommand(id));

		return ResponseEntity.ok(SportResponse.from(sport));
	}

	@PatchMapping("/{id}/deactivate")
	public ResponseEntity<SportResponse> deactivate(@PathVariable UUID id) {
		SportResult sport = commandBus.dispatch(new DeactivateSportCommand(id));

		return ResponseEntity.ok(SportResponse.from(sport));
	}

	@GetMapping
	public ResponseEntity<List<SportResponse>> list(@RequestParam(required = false) Boolean active) {
		List<SportResponse> sports = queryBus.ask(new ListSportsQuery(active))
				.stream()
				.map(SportResponse::from)
				.toList();

		return ResponseEntity.ok(sports);
	}
}
