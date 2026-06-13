package com.pabalvrz.sportsstatsapp.application.usecases;

import com.pabalvrz.sportsstatsapp.application.exception.SportNotFoundException;
import com.pabalvrz.sportsstatsapp.domain.model.Sport;
import com.pabalvrz.sportsstatsapp.domain.ports.in.GetSportByNameUseCase;
import com.pabalvrz.sportsstatsapp.domain.ports.out.SportRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetSportByNameUseCaseImpl implements GetSportByNameUseCase {

	private final SportRepositoryPort sportRepository;

	public GetSportByNameUseCaseImpl(SportRepositoryPort sportRepository) {
		this.sportRepository = sportRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public Sport execute(String name) {
		String normalizedName = Sport.normalizeName(name);

		return sportRepository.findByName(normalizedName)
				.orElseThrow(() -> new SportNotFoundException(normalizedName));
	}
}
