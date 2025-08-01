package org.withtime.be.withtimebe.domain.date.preference.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.withtime.be.withtimebe.domain.date.preference.entity.DatePreferenceDescription;

public interface DatePreferenceDescriptionRepository extends JpaRepository<DatePreferenceDescription, Long> {
}
