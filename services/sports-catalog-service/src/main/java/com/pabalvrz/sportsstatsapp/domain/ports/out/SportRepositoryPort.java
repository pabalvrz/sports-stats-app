package com.pabalvrz.sportsstatsapp.domain.ports.out;

import com.pabalvrz.sportsstatsapp.domain.model.Sport;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SportRepositoryPort {

	Sport save(Sport sport);

	Optional<Sport> findById(UUID id);

	List<Sport> findAll();
}
