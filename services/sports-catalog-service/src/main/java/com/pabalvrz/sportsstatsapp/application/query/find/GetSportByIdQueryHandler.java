package com.pabalvrz.sportsstatsapp.application.query.find;

import com.pabalvrz.sportsstatsapp.application.exception.SportNotFoundException;
import com.pabalvrz.sportsstatsapp.application.query.QueryHandler;
import com.pabalvrz.sportsstatsapp.application.result.SportResult;
import com.pabalvrz.sportsstatsapp.domain.ports.out.SportRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetSportByIdQueryHandler implements QueryHandler<GetSportByIdQuery, SportResult> {

	private final SportRepositoryPort sportRepository;

	public GetSportByIdQueryHandler(SportRepositoryPort sportRepository) {
		this.sportRepository = sportRepository;
	}

	@Override
	public Class<GetSportByIdQuery> queryType() {
		return GetSportByIdQuery.class;
	}

	@Override
	@Transactional(readOnly = true)
	public SportResult handle(GetSportByIdQuery query) {
		return sportRepository.findById(query.id())
				.map(SportResult::from)
				.orElseThrow(() -> new SportNotFoundException(query.id()));
	}
}
