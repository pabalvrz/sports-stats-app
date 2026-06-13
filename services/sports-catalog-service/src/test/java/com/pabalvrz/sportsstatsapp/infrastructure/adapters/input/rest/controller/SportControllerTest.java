package com.pabalvrz.sportsstatsapp.infrastructure.adapters.input.rest.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pabalvrz.sportsstatsapp.application.exception.SportNotFoundException;
import com.pabalvrz.sportsstatsapp.domain.model.Sport;
import com.pabalvrz.sportsstatsapp.domain.ports.in.CreateSportUseCase;
import com.pabalvrz.sportsstatsapp.domain.ports.in.GetSportByIdUseCase;
import com.pabalvrz.sportsstatsapp.domain.ports.in.ListSportsUseCase;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

@WebMvcTest(SportController.class)
class SportControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CreateSportUseCase createSportUseCase;

	@MockitoBean
	private GetSportByIdUseCase getSportByIdUseCase;

	@MockitoBean
	private ListSportsUseCase listSportsUseCase;

	@Test
	void createsSport() throws Exception {
		UUID id = UUID.randomUUID();
		when(createSportUseCase.execute(any())).thenReturn(Sport.reconstitute(id, "Football", true));

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
				.andExpect(jsonPath("$.title").value("Invalid request body"));
	}

	@Test
	void getsSportById() throws Exception {
		UUID id = UUID.randomUUID();
		when(getSportByIdUseCase.execute(id)).thenReturn(Sport.reconstitute(id, "Basketball", true));

		mockMvc.perform(get("/sports/{id}", id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(id.toString()))
				.andExpect(jsonPath("$.name").value("Basketball"))
				.andExpect(jsonPath("$.active").value(true));
	}

	@Test
	void returnsNotFoundForMissingSport() throws Exception {
		UUID id = UUID.randomUUID();
		when(getSportByIdUseCase.execute(id)).thenThrow(new SportNotFoundException(id));

		mockMvc.perform(get("/sports/{id}", id))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.title").value("Sport not found"));
	}

	@Test
	void listsSports() throws Exception {
		when(listSportsUseCase.execute()).thenReturn(List.of(
				Sport.reconstitute(UUID.randomUUID(), "Football", true),
				Sport.reconstitute(UUID.randomUUID(), "Tennis", false)
		));

		mockMvc.perform(get("/sports"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].name").value("Football"))
				.andExpect(jsonPath("$[1].name").value("Tennis"));
	}
}
