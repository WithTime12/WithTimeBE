package org.withtime.be.withtimebe.domain.date.preference.service.query;

import org.withtime.be.withtimebe.domain.date.preference.dto.DatePreferenceResponseDTO;
import org.withtime.be.withtimebe.domain.date.preference.entity.DatePreferenceTypeRelation;
import org.withtime.be.withtimebe.domain.date.preference.entity.enums.PreferenceRelationType;
import org.withtime.be.withtimebe.domain.date.preference.entity.enums.PreferenceType;

public interface DatePreferenceTypeRelationQueryService {

    DatePreferenceResponseDTO.FindRelationType findTypeRelations(PreferenceType preferenceType);
    DatePreferenceTypeRelation findTypeRelation(PreferenceType preferenceType, PreferenceRelationType preferenceRelationType);
}
