package com.pabalvrz.sportsstatsapp.application.usecases;

import com.pabalvrz.sportsstatsapp.application.exception.SportNotFoundException;
import com.pabalvrz.sportsstatsapp.domain.model.Sport;
import com.pabalvrz.sportsstatsapp.domain.ports.in.GetSportByIdUseCase;
import com.pabalvrz.sportsstatsapp.domain.ports.out.SportRepositoryPort;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetSportByIdUseCaseImpl implements GetSportByIdUseCase {

	private final SportRepositoryPort sportRepository;

	public GetSportByIdUseCaseImpl(SportRepositoryPort sportRepository) {
		this.sportRepository = sportRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public Sport execute(UUID id) {
		return sportRepository.findById(id).orElseThrow(() -> new SportNotFoundException(id));
	}
}
