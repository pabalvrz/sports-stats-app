package com.pabalvrz.sportsstatsapp.application.command.activate;

import com.pabalvrz.sportsstatsapp.application.command.CommandHandler;
import com.pabalvrz.sportsstatsapp.application.exception.SportNotFoundException;
import com.pabalvrz.sportsstatsapp.application.result.SportResult;
import com.pabalvrz.sportsstatsapp.domain.model.Sport;
import com.pabalvrz.sportsstatsapp.domain.ports.out.SportRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActivateSportCommandHandler implements CommandHandler<ActivateSportCommand, SportResult> {

	private final SportRepositoryPort sportRepository;

	public ActivateSportCommandHandler(SportRepositoryPort sportRepository) {
		this.sportRepository = sportRepository;
	}

	@Override
	public Class<ActivateSportCommand> commandType() {
		return ActivateSportCommand.class;
	}

	@Override
	@Transactional
	public SportResult handle(ActivateSportCommand command) {
		Sport sport = sportRepository.findById(command.id())
				.orElseThrow(() -> new SportNotFoundException(command.id()));

		return SportResult.from(sportRepository.save(sport.activate()));
	}
}
