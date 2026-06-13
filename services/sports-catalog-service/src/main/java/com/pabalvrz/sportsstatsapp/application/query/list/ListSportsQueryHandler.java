package com.pabalvrz.sportsstatsapp.application.query.list;

import com.pabalvrz.sportsstatsapp.application.query.QueryHandler;
import com.pabalvrz.sportsstatsapp.application.result.SportResult;
import com.pabalvrz.sportsstatsapp.domain.ports.out.SportRepositoryPort;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListSportsQueryHandler implements QueryHandler<ListSportsQuery, List<SportResult>> {

	private final SportRepositoryPort sportRepository;

	public ListSportsQueryHandler(SportRepositoryPort sportRepository) {
		this.sportRepository = sportRepository;
	}

	@Override
	public Class<ListSportsQuery> queryType() {
		return ListSportsQuery.class;
	}

	@Override
	@Transactional(readOnly = true)
	public List<SportResult> handle(ListSportsQuery query) {
		return sportRepository.findAll()
				.stream()
				.map(SportResult::from)
				.toList();
	}
}
