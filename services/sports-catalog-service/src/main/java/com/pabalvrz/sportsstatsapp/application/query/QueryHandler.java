package com.pabalvrz.sportsstatsapp.application.query;

public interface QueryHandler<Q extends Query<R>, R> {

	Class<Q> queryType();

	R handle(Q query);
}
