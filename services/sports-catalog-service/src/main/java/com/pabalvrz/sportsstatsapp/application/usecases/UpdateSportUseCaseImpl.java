package com.pabalvrz.sportsstatsapp.application.usecases;

import com.pabalvrz.sportsstatsapp.application.exception.SportNotFoundException;
import com.pabalvrz.sportsstatsapp.domain.model.Sport;
import com.pabalvrz.sportsstatsapp.domain.ports.in.UpdateSportUseCase;
import com.pabalvrz.sportsstatsapp.domain.ports.out.SportRepositoryPort;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateSportUseCaseImpl implements UpdateSportUseCase {

	private final SportRepositoryPort sportRepository;

	public UpdateSportUseCaseImpl(SportRepositoryPort sportRepository) {
		this.sportRepository = sportRepository;
	}

	@Override
	@Transactional
	public Sport execute(UUID id, String name) {
		Sport sport = sportRepository.findById(id).orElseThrow(() -> new SportNotFoundException(id));

		return sportRepository.save(sport.rename(name));
	}
}
