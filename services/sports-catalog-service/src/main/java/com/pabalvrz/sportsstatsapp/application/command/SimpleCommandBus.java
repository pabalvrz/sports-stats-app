package com.pabalvrz.sportsstatsapp.application.command;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class SimpleCommandBus implements CommandBus {

	private final Map<Class<?>, CommandHandler<?, ?>> handlers;

	public SimpleCommandBus(List<CommandHandler<?, ?>> handlers) {
		this.handlers = handlers.stream()
				.collect(Collectors.toUnmodifiableMap(CommandHandler::commandType, Function.identity()));
	}

	@Override
	public <R> R dispatch(Command<R> command) {
		CommandHandler<Command<R>, R> handler = findHandler(command);

		return handler.handle(command);
	}

	@SuppressWarnings("unchecked")
	private <R> CommandHandler<Command<R>, R> findHandler(Command<R> command) {
		CommandHandler<?, ?> handler = handlers.get(command.getClass());
		if (handler == null) {
			throw new IllegalArgumentException("No command handler registered for " + command.getClass().getName());
		}

		return (CommandHandler<Command<R>, R>) handler;
	}
}
