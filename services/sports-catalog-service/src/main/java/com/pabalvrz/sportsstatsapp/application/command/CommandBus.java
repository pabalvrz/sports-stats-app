package com.pabalvrz.sportsstatsapp.application.command;

public interface CommandBus {

	<R> R dispatch(Command<R> command);
}
