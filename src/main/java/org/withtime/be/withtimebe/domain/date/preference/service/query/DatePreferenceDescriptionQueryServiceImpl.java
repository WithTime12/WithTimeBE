package org.withtime.be.withtimebe.domain.date.preference.service.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.withtime.be.withtimebe.domain.date.preference.entity.DatePreferenceDescription;
import org.withtime.be.withtimebe.domain.date.preference.repository.DatePreferenceDescriptionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DatePreferenceDescriptionQueryServiceImpl implements DatePreferenceDescriptionQueryService {

    private final DatePreferenceDescriptionRepository datePreferenceDescriptionRepository;

    @Override
    public List<DatePreferenceDescription> findTypes() {
        return datePreferenceDescriptionRepository.findAll();
    }

}
