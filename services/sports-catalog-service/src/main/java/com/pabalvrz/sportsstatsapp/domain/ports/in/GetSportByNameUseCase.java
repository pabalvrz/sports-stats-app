package com.pabalvrz.sportsstatsapp.domain.ports.in;

import com.pabalvrz.sportsstatsapp.domain.model.Sport;

public interface GetSportByNameUseCase {

	Sport execute(String name);
}
