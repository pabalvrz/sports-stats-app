package com.pabalvrz.sportsstatsapp.application.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pabalvrz.sportsstatsapp.application.command.create.CreateSportCommand;
import com.pabalvrz.sportsstatsapp.application.command.update.UpdateSportCommand;
import com.pabalvrz.sportsstatsapp.application.result.SportResult;
import com.pabalvrz.sportsstatsapp.domain.model.Sport;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class SimpleCommandBusTest {

	private final TestCreateSportCommandHandler createSportCommandHandler = new TestCreateSportCommandHandler();
	private final TestUpdateSportCommandHandler updateSportCommandHandler = new TestUpdateSportCommandHandler();
	private final SimpleCommandBus commandBus = new SimpleCommandBus(List.of(createSportCommandHandler, updateSportCommandHandler));

	@Test
	void dispatchesCreateSportCommand() {
		SportResult sport = commandBus.dispatch(new CreateSportCommand("Football"));

		assertThat(sport.name()).isEqualTo("Football");
		assertThat(createSportCommandHandler.handledCommand).isEqualTo(new CreateSportCommand("Football"));
	}

	@Test
	void dispatchesUpdateSportCommand() {
		UUID id = UUID.randomUUID();

		SportResult sport = commandBus.dispatch(new UpdateSportCommand(id, "Tennis"));

		assertThat(sport.id()).isEqualTo(id);
		assertThat(sport.name()).isEqualTo("Tennis");
		assertThat(updateSportCommandHandler.handledCommand).isEqualTo(new UpdateSportCommand(id, "Tennis"));
	}

	@Test
	void failsWhenNoHandlerIsRegistered() {
		SimpleCommandBus emptyCommandBus = new SimpleCommandBus(List.of());

		assertThatThrownBy(() -> emptyCommandBus.dispatch(new TestCommand()))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining(TestCommand.class.getName());
	}

	private record TestCommand() implements Command<String> {
	}

	private static final class TestCreateSportCommandHandler implements CommandHandler<CreateSportCommand, SportResult> {

		private CreateSportCommand handledCommand;

		@Override
		public Class<CreateSportCommand> commandType() {
			return CreateSportCommand.class;
		}

		@Override
		public SportResult handle(CreateSportCommand command) {
			this.handledCommand = command;
			return SportResult.from(Sport.create(command.name()));
		}
	}

	private static final class TestUpdateSportCommandHandler implements CommandHandler<UpdateSportCommand, SportResult> {

		private UpdateSportCommand handledCommand;

		@Override
		public Class<UpdateSportCommand> commandType() {
			return UpdateSportCommand.class;
		}

		@Override
		public SportResult handle(UpdateSportCommand command) {
			this.handledCommand = command;
			return SportResult.from(Sport.reconstitute(command.id(), command.name(), true));
		}
	}
}
