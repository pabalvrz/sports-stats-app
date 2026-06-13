package com.pabalvrz.sportsstatsapp.application.query;

public interface QueryBus {

	<R> R ask(Query<R> query);
}
