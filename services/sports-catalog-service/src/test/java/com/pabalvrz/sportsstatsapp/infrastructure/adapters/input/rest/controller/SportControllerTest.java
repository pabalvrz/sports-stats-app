package com.pabalvrz.sportsstatsapp.infrastructure.adapters.input.rest.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pabalvrz.sportsstatsapp.application.command.CommandBus;
import com.pabalvrz.sportsstatsapp.application.command.activate.ActivateSportCommand;
import com.pabalvrz.sportsstatsapp.application.command.create.CreateSportCommand;
import com.pabalvrz.sportsstatsapp.application.command.deactivate.DeactivateSportCommand;
import com.pabalvrz.sportsstatsapp.application.command.update.UpdateSportCommand;
import com.pabalvrz.sportsstatsapp.application.exception.SportNotFoundException;
import com.pabalvrz.sportsstatsapp.application.query.QueryBus;
import com.pabalvrz.sportsstatsapp.application.query.find.GetSportByIdQuery;
import com.pabalvrz.sportsstatsapp.application.query.find.GetSportByNameQuery;
import com.pabalvrz.sportsstatsapp.application.query.list.ListSportsQuery;
import com.pabalvrz.sportsstatsapp.application.result.SportResult;
import com.pabalvrz.sportsstatsapp.domain.exception.SportIdentifierRequiredException;
import com.pabalvrz.sportsstatsapp.domain.exception.SportNameRequiredException;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SportController.class)
class SportControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CommandBus commandBus;

	@MockitoBean
	private QueryBus queryBus;

	@Test
	void createsSport() throws Exception {
		UUID id = UUID.randomUUID();
		when(commandBus.dispatch(new CreateSportCommand("Football")))
				.thenReturn(new SportResult(id, "Football", true));

		mockMvc.perform(post("/sports")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"name":"Football"}
								"""))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "http://localhost/sports/" + id))
				.andExpect(jsonPath("$.id").value(id.toString()))
				.andExpect(jsonPath("$.name").value("Football"))
				.andExpect(jsonPath("$.active").value(true));
	}

	@Test
	void rejectsInvalidCreateRequest() throws Exception {
		mockMvc.perform(post("/sports")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"name":""}
				"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.error").value("Bad Request"))
				.andExpect(jsonPath("$.message").value("Request validation failed"))
				.andExpect(jsonPath("$.path").value("/sports"))
				.andExpect(jsonPath("$.fieldErrors[0].field").value("name"))
				.andExpect(jsonPath("$.fieldErrors[0].message").value("name must not be blank"));
	}

	@Test
	void returnsBadRequestForInvalidDomainSportName() throws Exception {
		when(commandBus.dispatch(new CreateSportCommand("Football"))).thenThrow(new SportNameRequiredException());

		mockMvc.perform(post("/sports")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"name":"Football"}
								"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.error").value("Bad Request"))
				.andExpect(jsonPath("$.message").value("Sport name is required"))
				.andExpect(jsonPath("$.path").value("/sports"));
	}

	@Test
	void returnsBadRequestForMissingDomainSportIdentifier() throws Exception {
		UUID id = UUID.randomUUID();
		when(commandBus.dispatch(new ActivateSportCommand(id))).thenThrow(new SportIdentifierRequiredException());

		mockMvc.perform(patch("/sports/{id}/activate", id))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.error").value("Bad Request"))
				.andExpect(jsonPath("$.message").value("Sport identifier is required"))
				.andExpect(jsonPath("$.path").value("/sports/" + id + "/activate"));
	}

	@Test
	void returnsConflictForDuplicateSportName() throws Exception {
		when(commandBus.dispatch(new CreateSportCommand("Football")))
				.thenThrow(new DataIntegrityViolationException("duplicate key value violates unique constraint"));

		mockMvc.perform(post("/sports")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"name":"Football"}
								"""))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.status").value(409))
				.andExpect(jsonPath("$.error").value("Conflict"))
				.andExpect(jsonPath("$.message").value("Sport name already exists"))
				.andExpect(jsonPath("$.path").value("/sports"));
	}

	@Test
	void returnsServerErrorForMissingCommandHandler() throws Exception {
		when(commandBus.dispatch(new CreateSportCommand("Football")))
				.thenThrow(new IllegalArgumentException("No command handler registered for "
						+ CreateSportCommand.class.getName()));

		mockMvc.perform(post("/sports")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"name":"Football"}
								"""))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status").value(500))
				.andExpect(jsonPath("$.error").value("Internal Server Error"))
				.andExpect(jsonPath("$.message").value("Application handler is not configured"))
				.andExpect(jsonPath("$.path").value("/sports"));
	}

	@Test
	void returnsServerErrorForMissingQueryHandler() throws Exception {
		when(queryBus.ask(new ListSportsQuery(null)))
				.thenThrow(new IllegalArgumentException("No query handler registered for "
						+ ListSportsQuery.class.getName()));

		mockMvc.perform(get("/sports"))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status").value(500))
				.andExpect(jsonPath("$.error").value("Internal Server Error"))
				.andExpect(jsonPath("$.message").value("Application handler is not configured"))
				.andExpect(jsonPath("$.path").value("/sports"));
	}

	@Test
	void returnsSafeServerErrorForUnexpectedErrors() throws Exception {
		when(queryBus.ask(new ListSportsQuery(null))).thenThrow(new RuntimeException("database credentials leaked"));

		mockMvc.perform(get("/sports"))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status").value(500))
				.andExpect(jsonPath("$.error").value("Internal Server Error"))
				.andExpect(jsonPath("$.message").value("An unexpected error occurred"))
				.andExpect(jsonPath("$.path").value("/sports"));
	}

	@Test
	void getsSportById() throws Exception {
		UUID id = UUID.randomUUID();
		when(queryBus.ask(new GetSportByIdQuery(id))).thenReturn(new SportResult(id, "Basketball", true));

		mockMvc.perform(get("/sports/{id}", id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(id.toString()))
				.andExpect(jsonPath("$.name").value("Basketball"))
				.andExpect(jsonPath("$.active").value(true));
	}

	@Test
	void returnsNotFoundForMissingSport() throws Exception {
		UUID id = UUID.randomUUID();
		when(queryBus.ask(new GetSportByIdQuery(id))).thenThrow(new SportNotFoundException(id));

		mockMvc.perform(get("/sports/{id}", id))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.error").value("Not Found"))
				.andExpect(jsonPath("$.message").value("Sport not found: " + id))
				.andExpect(jsonPath("$.path").value("/sports/" + id));
	}

	@Test
	void getsSportByName() throws Exception {
		UUID id = UUID.randomUUID();
		when(queryBus.ask(new GetSportByNameQuery("Football"))).thenReturn(new SportResult(id, "Football", true));

		mockMvc.perform(get("/sports/by-name/{name}", "Football"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(id.toString()))
				.andExpect(jsonPath("$.name").value("Football"))
				.andExpect(jsonPath("$.active").value(true));
	}

	@Test
	void returnsNotFoundForMissingSportName() throws Exception {
		when(queryBus.ask(new GetSportByNameQuery("Football"))).thenThrow(new SportNotFoundException("Football"));

		mockMvc.perform(get("/sports/by-name/{name}", "Football"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.error").value("Not Found"))
				.andExpect(jsonPath("$.message").value("Sport not found: Football"))
				.andExpect(jsonPath("$.path").value("/sports/by-name/Football"));
	}

	@Test
	void updatesSport() throws Exception {
		UUID id = UUID.randomUUID();
		when(commandBus.dispatch(new UpdateSportCommand(id, "Tennis")))
				.thenReturn(new SportResult(id, "Tennis", true));

		mockMvc.perform(put("/sports/{id}", id)
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"name":"Tennis"}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(id.toString()))
				.andExpect(jsonPath("$.name").value("Tennis"))
				.andExpect(jsonPath("$.active").value(true));
	}

	@Test
	void rejectsInvalidUpdateRequest() throws Exception {
		UUID id = UUID.randomUUID();

		mockMvc.perform(put("/sports/{id}", id)
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"name":""}
				"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.error").value("Bad Request"))
				.andExpect(jsonPath("$.message").value("Request validation failed"))
				.andExpect(jsonPath("$.path").value("/sports/" + id))
				.andExpect(jsonPath("$.fieldErrors[0].field").value("name"))
				.andExpect(jsonPath("$.fieldErrors[0].message").value("name must not be blank"));
	}

	@Test
	void returnsNotFoundWhenUpdatingMissingSport() throws Exception {
		UUID id = UUID.randomUUID();
		when(commandBus.dispatch(new UpdateSportCommand(id, "Tennis"))).thenThrow(new SportNotFoundException(id));

		mockMvc.perform(put("/sports/{id}", id)
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"name":"Tennis"}
				"""))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.error").value("Not Found"))
				.andExpect(jsonPath("$.message").value("Sport not found: " + id))
				.andExpect(jsonPath("$.path").value("/sports/" + id));
	}

	@Test
	void activatesSport() throws Exception {
		UUID id = UUID.randomUUID();
		when(commandBus.dispatch(new ActivateSportCommand(id))).thenReturn(new SportResult(id, "Football", true));

		mockMvc.perform(patch("/sports/{id}/activate", id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(id.toString()))
				.andExpect(jsonPath("$.name").value("Football"))
				.andExpect(jsonPath("$.active").value(true));
	}

	@Test
	void returnsNotFoundWhenActivatingMissingSport() throws Exception {
		UUID id = UUID.randomUUID();
		when(commandBus.dispatch(new ActivateSportCommand(id))).thenThrow(new SportNotFoundException(id));

		mockMvc.perform(patch("/sports/{id}/activate", id))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.error").value("Not Found"))
				.andExpect(jsonPath("$.message").value("Sport not found: " + id))
				.andExpect(jsonPath("$.path").value("/sports/" + id + "/activate"));
	}

	@Test
	void deactivatesSport() throws Exception {
		UUID id = UUID.randomUUID();
		when(commandBus.dispatch(new DeactivateSportCommand(id))).thenReturn(new SportResult(id, "Football", false));

		mockMvc.perform(patch("/sports/{id}/deactivate", id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(id.toString()))
				.andExpect(jsonPath("$.name").value("Football"))
				.andExpect(jsonPath("$.active").value(false));
	}

	@Test
	void returnsNotFoundWhenDeactivatingMissingSport() throws Exception {
		UUID id = UUID.randomUUID();
		when(commandBus.dispatch(new DeactivateSportCommand(id))).thenThrow(new SportNotFoundException(id));

		mockMvc.perform(patch("/sports/{id}/deactivate", id))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.error").value("Not Found"))
				.andExpect(jsonPath("$.message").value("Sport not found: " + id))
				.andExpect(jsonPath("$.path").value("/sports/" + id + "/deactivate"));
	}

	@Test
	void listsSports() throws Exception {
		when(queryBus.ask(new ListSportsQuery(null))).thenReturn(List.of(
				new SportResult(UUID.randomUUID(), "Football", true),
				new SportResult(UUID.randomUUID(), "Tennis", false)
		));

		mockMvc.perform(get("/sports"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].name").value("Football"))
				.andExpect(jsonPath("$[1].name").value("Tennis"));
	}

	@Test
	void listsSportsFilteredByActiveStatus() throws Exception {
		when(queryBus.ask(new ListSportsQuery(true))).thenReturn(List.of(
				new SportResult(UUID.randomUUID(), "Football", true)
		));

		mockMvc.perform(get("/sports").param("active", "true"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].name").value("Football"))
				.andExpect(jsonPath("$[0].active").value(true));
	}

	@Test
	void listsSportsFilteredByInactiveStatus() throws Exception {
		when(queryBus.ask(new ListSportsQuery(false))).thenReturn(List.of(
				new SportResult(UUID.randomUUID(), "Tennis", false)
		));

		mockMvc.perform(get("/sports").param("active", "false"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].name").value("Tennis"))
				.andExpect(jsonPath("$[0].active").value(false));
	}
}
