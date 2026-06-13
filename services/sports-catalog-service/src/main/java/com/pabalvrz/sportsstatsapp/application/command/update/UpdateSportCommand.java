package com.pabalvrz.sportsstatsapp.application.command.update;

import com.pabalvrz.sportsstatsapp.application.command.Command;
import com.pabalvrz.sportsstatsapp.application.result.SportResult;
import java.util.UUID;

public record UpdateSportCommand(UUID id, String name) implements Command<SportResult> {
}
