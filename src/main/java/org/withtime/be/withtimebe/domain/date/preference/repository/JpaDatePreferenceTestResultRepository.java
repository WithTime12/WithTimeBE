package org.withtime.be.withtimebe.domain.date.preference.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.withtime.be.withtimebe.domain.date.preference.entity.DatePreferenceTestResult;

public interface JpaDatePreferenceTestResultRepository extends DatePreferenceTestResultRepository, JpaRepository<DatePreferenceTestResult, Long> {
}
