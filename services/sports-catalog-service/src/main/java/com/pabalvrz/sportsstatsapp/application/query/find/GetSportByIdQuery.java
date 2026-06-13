package com.pabalvrz.sportsstatsapp.application.query.find;

import com.pabalvrz.sportsstatsapp.application.query.Query;
import com.pabalvrz.sportsstatsapp.application.result.SportResult;
import java.util.UUID;

public record GetSportByIdQuery(UUID id) implements Query<SportResult> {
}
