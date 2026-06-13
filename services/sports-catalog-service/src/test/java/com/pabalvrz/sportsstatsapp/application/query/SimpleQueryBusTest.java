package com.pabalvrz.sportsstatsapp.application.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;

class SimpleQueryBusTest {

	@Test
	void dispatchesQueryToMatchingHandler() {
		SimpleQueryBus queryBus = new SimpleQueryBus(List.of(new TestQueryHandler()));

		String result = queryBus.ask(new TestQuery("Football"));

		assertThat(result).isEqualTo("found Football");
	}

	@Test
	void failsWhenNoHandlerIsRegistered() {
		SimpleQueryBus queryBus = new SimpleQueryBus(List.of());

		assertThatThrownBy(() -> queryBus.ask(new TestQuery("Football")))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining(TestQuery.class.getName());
	}

	private record TestQuery(String name) implements Query<String> {
	}

	private static final class TestQueryHandler implements QueryHandler<TestQuery, String> {

		@Override
		public Class<TestQuery> queryType() {
			return TestQuery.class;
		}

		@Override
		public String handle(TestQuery query) {
			return "found " + query.name();
		}
	}
}
