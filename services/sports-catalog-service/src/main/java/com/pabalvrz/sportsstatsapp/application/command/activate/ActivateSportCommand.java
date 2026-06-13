package com.pabalvrz.sportsstatsapp.application.command.activate;

import com.pabalvrz.sportsstatsapp.application.command.Command;
import com.pabalvrz.sportsstatsapp.application.result.SportResult;
import java.util.UUID;

public record ActivateSportCommand(UUID id) implements Command<SportResult> {
}
