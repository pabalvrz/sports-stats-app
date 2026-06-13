package com.pabalvrz.sportsstatsapp.application.usecases;

import com.pabalvrz.sportsstatsapp.domain.model.Sport;
import com.pabalvrz.sportsstatsapp.domain.ports.in.ListSportsUseCase;
import com.pabalvrz.sportsstatsapp.domain.ports.out.SportRepositoryPort;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListSportsUseCaseImpl implements ListSportsUseCase {

	private final SportRepositoryPort sportRepository;

	public ListSportsUseCaseImpl(SportRepositoryPort sportRepository) {
		this.sportRepository = sportRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Sport> execute() {
		return sportRepository.findAll();
	}
}
