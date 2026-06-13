package com.pabalvrz.sportsstatsapp.application.query.find;

import com.pabalvrz.sportsstatsapp.application.exception.SportNotFoundException;
import com.pabalvrz.sportsstatsapp.application.query.QueryHandler;
import com.pabalvrz.sportsstatsapp.application.result.SportResult;
import com.pabalvrz.sportsstatsapp.domain.ports.out.SportRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetSportByNameQueryHandler implements QueryHandler<GetSportByNameQuery, SportResult> {

	private final SportRepositoryPort sportRepository;

	public GetSportByNameQueryHandler(SportRepositoryPort sportRepository) {
		this.sportRepository = sportRepository;
	}

	@Override
	public Class<GetSportByNameQuery> queryType() {
		return GetSportByNameQuery.class;
	}

	@Override
	@Transactional(readOnly = true)
	public SportResult handle(GetSportByNameQuery query) {
		return sportRepository.findByName(query.name())
				.map(SportResult::from)
				.orElseThrow(() -> new SportNotFoundException(query.name()));
	}
}
