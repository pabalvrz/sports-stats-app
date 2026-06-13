package com.pabalvrz.sportsstatsapp.application.command.deactivate;

import com.pabalvrz.sportsstatsapp.application.command.Command;
import com.pabalvrz.sportsstatsapp.application.result.SportResult;
import java.util.UUID;

public record DeactivateSportCommand(UUID id) implements Command<SportResult> {
}
