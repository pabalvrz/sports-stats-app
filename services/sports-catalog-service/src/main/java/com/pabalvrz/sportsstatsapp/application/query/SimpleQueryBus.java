package com.pabalvrz.sportsstatsapp.application.query;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class SimpleQueryBus implements QueryBus {

	private final Map<Class<?>, QueryHandler<?, ?>> handlers;

	public SimpleQueryBus(List<QueryHandler<?, ?>> handlers) {
		this.handlers = handlers.stream()
				.collect(Collectors.toUnmodifiableMap(QueryHandler::queryType, Function.identity()));
	}

	@Override
	public <R> R ask(Query<R> query) {
		QueryHandler<Query<R>, R> handler = findHandler(query);

		return handler.handle(query);
	}

	@SuppressWarnings("unchecked")
	private <R> QueryHandler<Query<R>, R> findHandler(Query<R> query) {
		QueryHandler<?, ?> handler = handlers.get(query.getClass());
		if (handler == null) {
			throw new IllegalArgumentException("No query handler registered for " + query.getClass().getName());
		}

		return (QueryHandler<Query<R>, R>) handler;
	}
}
