package org.withtime.be.withtimebe.domain.date.service.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.withtime.be.withtimebe.domain.date.entity.DatePlace;
import org.withtime.be.withtimebe.domain.date.repository.DatePlaceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DatePlaceQueryServiceImpl implements DatePlaceQueryService {

	private final DatePlaceRepository datePlaceRepository;

	@Override
	public Page<DatePlace> findDatePlaces(Pageable pageable) {
		return datePlaceRepository.findAll(pageable);
	}
}
