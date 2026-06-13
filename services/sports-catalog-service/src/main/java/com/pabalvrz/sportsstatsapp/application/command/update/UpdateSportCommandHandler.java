package com.pabalvrz.sportsstatsapp.application.command.update;

import com.pabalvrz.sportsstatsapp.application.command.CommandHandler;
import com.pabalvrz.sportsstatsapp.application.exception.SportNotFoundException;
import com.pabalvrz.sportsstatsapp.application.result.SportResult;
import com.pabalvrz.sportsstatsapp.domain.model.Sport;
import com.pabalvrz.sportsstatsapp.domain.ports.out.SportRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateSportCommandHandler implements CommandHandler<UpdateSportCommand, SportResult> {

	private final SportRepositoryPort sportRepository;

	public UpdateSportCommandHandler(SportRepositoryPort sportRepository) {
		this.sportRepository = sportRepository;
	}

	@Override
	public Class<UpdateSportCommand> commandType() {
		return UpdateSportCommand.class;
	}

	@Override
	@Transactional
	public SportResult handle(UpdateSportCommand command) {
		Sport sport = sportRepository.findById(command.id())
				.orElseThrow(() -> new SportNotFoundException(command.id()));

		return SportResult.from(sportRepository.save(sport.rename(command.name())));
	}
}
