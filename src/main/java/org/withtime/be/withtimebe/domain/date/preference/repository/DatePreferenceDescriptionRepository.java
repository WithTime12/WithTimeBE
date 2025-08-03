package org.withtime.be.withtimebe.domain.date.preference.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.withtime.be.withtimebe.domain.date.preference.entity.DatePreferenceDescription;
import org.withtime.be.withtimebe.domain.date.preference.entity.enums.PreferenceType;

import java.util.Optional;

public interface DatePreferenceDescriptionRepository extends JpaRepository<DatePreferenceDescription, Long> {
    Optional<DatePreferenceDescription> findByPreferenceType(PreferenceType preferenceType);
}
