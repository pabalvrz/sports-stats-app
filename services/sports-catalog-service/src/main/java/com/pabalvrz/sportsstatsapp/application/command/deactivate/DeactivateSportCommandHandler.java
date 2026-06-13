package com.pabalvrz.sportsstatsapp.application.command.deactivate;

import com.pabalvrz.sportsstatsapp.application.command.CommandHandler;
import com.pabalvrz.sportsstatsapp.application.exception.SportNotFoundException;
import com.pabalvrz.sportsstatsapp.application.result.SportResult;
import com.pabalvrz.sportsstatsapp.domain.model.Sport;
import com.pabalvrz.sportsstatsapp.domain.ports.out.SportRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeactivateSportCommandHandler implements CommandHandler<DeactivateSportCommand, SportResult> {

	private final SportRepositoryPort sportRepository;

	public DeactivateSportCommandHandler(SportRepositoryPort sportRepository) {
		this.sportRepository = sportRepository;
	}

	@Override
	public Class<DeactivateSportCommand> commandType() {
		return DeactivateSportCommand.class;
	}

	@Override
	@Transactional
	public SportResult handle(DeactivateSportCommand command) {
		Sport sport = sportRepository.findById(command.id())
				.orElseThrow(() -> new SportNotFoundException(command.id()));

		return SportResult.from(sportRepository.save(sport.deactivate()));
	}
}
