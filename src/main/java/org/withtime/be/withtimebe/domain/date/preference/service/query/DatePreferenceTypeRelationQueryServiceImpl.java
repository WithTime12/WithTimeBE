package org.withtime.be.withtimebe.domain.date.preference.service.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.withtime.be.withtimebe.domain.date.preference.converter.DatePreferenceConverter;
import org.withtime.be.withtimebe.domain.date.preference.dto.DatePreferenceResponseDTO;
import org.withtime.be.withtimebe.domain.date.preference.entity.DatePreferenceDescription;
import org.withtime.be.withtimebe.domain.date.preference.entity.DatePreferenceTypeRelation;
import org.withtime.be.withtimebe.domain.date.preference.entity.enums.PreferenceRelationType;
import org.withtime.be.withtimebe.domain.date.preference.entity.enums.PreferenceType;
import org.withtime.be.withtimebe.domain.date.preference.repository.DatePreferenceDescriptionRepository;
import org.withtime.be.withtimebe.domain.date.preference.repository.DatePreferenceTypeRelationRepository;
import org.withtime.be.withtimebe.global.error.code.DatePreferenceErrorCode;
import org.withtime.be.withtimebe.global.error.exception.DatePreferenceException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DatePreferenceTypeRelationQueryServiceImpl implements DatePreferenceTypeRelationQueryService {

    private final DatePreferenceTypeRelationRepository datePreferenceTypeRelationRepository;
    private final DatePreferenceDescriptionRepository datePreferenceDescriptionRepository;

    @Override
    public DatePreferenceResponseDTO.FindRelationType findTypeRelations(PreferenceType preferenceType) {
        DatePreferenceTypeRelation best = findTypeRelation(preferenceType, PreferenceRelationType.BEST);
        DatePreferenceTypeRelation worst = findTypeRelation(preferenceType, PreferenceRelationType.WORST);

        return DatePreferenceConverter.toFindRelationType(
                best,
                worst,
                datePreferenceDescriptionRepository.findByPreferenceType(best.getType()).orElseThrow(() -> new DatePreferenceException(DatePreferenceErrorCode.NOT_FOUND_DESCRIPTION)),
                datePreferenceDescriptionRepository.findByPreferenceType(worst.getType()).orElseThrow(() -> new DatePreferenceException(DatePreferenceErrorCode.NOT_FOUND_DESCRIPTION))
        );
    }

    @Override
    public DatePreferenceTypeRelation findTypeRelation(PreferenceType preferenceType, PreferenceRelationType preferenceRelationType) {
        return datePreferenceTypeRelationRepository.findByTargetTypeAndPreferenceRelationType(preferenceType, preferenceRelationType)
                .orElseThrow(() -> new DatePreferenceException(DatePreferenceErrorCode.NOT_FOUND_RELATION));
    }
}
