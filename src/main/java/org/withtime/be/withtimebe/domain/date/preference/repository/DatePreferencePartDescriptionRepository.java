package org.withtime.be.withtimebe.domain.date.preference.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.withtime.be.withtimebe.domain.date.preference.entity.DatePreferencePartDescription;
import org.withtime.be.withtimebe.domain.date.preference.entity.enums.PreferencePartType;

import java.util.Optional;

public interface DatePreferencePartDescriptionRepository extends JpaRepository<DatePreferencePartDescription, Long> {
    Optional<DatePreferencePartDescription> findByPreferencePartType(PreferencePartType preferencePartType);
}
