package com.pabalvrz.sportsstatsapp.application.command.create;

import com.pabalvrz.sportsstatsapp.application.command.CommandHandler;
import com.pabalvrz.sportsstatsapp.application.result.SportResult;
import com.pabalvrz.sportsstatsapp.domain.model.Sport;
import com.pabalvrz.sportsstatsapp.domain.ports.out.SportRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateSportCommandHandler implements CommandHandler<CreateSportCommand, SportResult> {

	private final SportRepositoryPort sportRepository;

	public CreateSportCommandHandler(SportRepositoryPort sportRepository) {
		this.sportRepository = sportRepository;
	}

	@Override
	public Class<CreateSportCommand> commandType() {
		return CreateSportCommand.class;
	}

	@Override
	@Transactional
	public SportResult handle(CreateSportCommand command) {
		Sport sport = sportRepository.save(Sport.create(command.name()));

		return SportResult.from(sport);
	}
}
