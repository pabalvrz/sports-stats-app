package com.pabalvrz.sportsstatsapp.application.query.find;

import com.pabalvrz.sportsstatsapp.application.query.Query;
import com.pabalvrz.sportsstatsapp.application.result.SportResult;

public record GetSportByNameQuery(String name) implements Query<SportResult> {
}
