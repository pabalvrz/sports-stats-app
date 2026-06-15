package com.pabalvrz.sportsstatsapp.application.query.list;

import com.pabalvrz.sportsstatsapp.application.query.Query;
import com.pabalvrz.sportsstatsapp.application.result.SportResult;
import java.util.List;

public record ListSportsQuery(Boolean active) implements Query<List<SportResult>> {
}
