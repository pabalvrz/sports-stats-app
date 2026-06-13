package com.pabalvrz.sportsstatsapp.infrastructure.adapters.input.rest.request;

import jakarta.validation.constraints.NotBlank;

public record CreateSportRequest(@NotBlank(message = "name must not be blank") String name) {
}
