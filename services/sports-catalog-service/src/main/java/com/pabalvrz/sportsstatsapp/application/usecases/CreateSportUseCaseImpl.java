package com.pabalvrz.sportsstatsapp.application.usecases;

import com.pabalvrz.sportsstatsapp.domain.model.Sport;
import com.pabalvrz.sportsstatsapp.domain.ports.in.CreateSportUseCase;
import com.pabalvrz.sportsstatsapp.domain.ports.out.SportRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateSportUseCaseImpl implements CreateSportUseCase {

	private final SportRepositoryPort sportRepository;

	public CreateSportUseCaseImpl(SportRepositoryPort sportRepository) {
		this.sportRepository = sportRepository;
	}

	@Override
	@Transactional
	public Sport execute(String name) {
		return sportRepository.save(Sport.create(name));
	}
}
