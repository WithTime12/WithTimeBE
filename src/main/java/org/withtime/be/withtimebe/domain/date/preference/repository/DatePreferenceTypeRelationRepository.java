package org.withtime.be.withtimebe.domain.date.preference.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.withtime.be.withtimebe.domain.date.preference.entity.DatePreferenceTypeRelation;
import org.withtime.be.withtimebe.domain.date.preference.entity.enums.PreferenceRelationType;
import org.withtime.be.withtimebe.domain.date.preference.entity.enums.PreferenceType;

import java.util.Optional;

public interface DatePreferenceTypeRelationRepository extends JpaRepository<DatePreferenceTypeRelation, Long> {

    Optional<DatePreferenceTypeRelation> findByTargetTypeAndPreferenceRelationType(PreferenceType targetType, PreferenceRelationType preferenceRelationType);
}
