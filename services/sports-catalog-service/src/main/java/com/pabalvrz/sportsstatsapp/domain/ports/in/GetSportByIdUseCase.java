package com.pabalvrz.sportsstatsapp.domain.ports.in;

import com.pabalvrz.sportsstatsapp.domain.model.Sport;
import java.util.UUID;

public interface GetSportByIdUseCase {

	Sport execute(UUID id);
}
