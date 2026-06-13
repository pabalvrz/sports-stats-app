package com.pabalvrz.sportsstatsapp.domain.ports.in;

import com.pabalvrz.sportsstatsapp.domain.model.Sport;

public interface CreateSportUseCase {

	Sport execute(String name);
}
