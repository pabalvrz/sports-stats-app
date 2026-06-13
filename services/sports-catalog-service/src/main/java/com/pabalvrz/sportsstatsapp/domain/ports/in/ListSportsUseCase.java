package com.pabalvrz.sportsstatsapp.domain.ports.in;

import com.pabalvrz.sportsstatsapp.domain.model.Sport;
import java.util.List;

public interface ListSportsUseCase {

	List<Sport> execute();
}
