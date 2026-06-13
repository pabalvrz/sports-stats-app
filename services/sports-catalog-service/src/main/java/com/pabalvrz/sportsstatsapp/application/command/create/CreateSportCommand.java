package com.pabalvrz.sportsstatsapp.application.command.create;

import com.pabalvrz.sportsstatsapp.application.command.Command;
import com.pabalvrz.sportsstatsapp.application.result.SportResult;

public record CreateSportCommand(String name) implements Command<SportResult> {
}
