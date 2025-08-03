package org.withtime.be.withtimebe.domain.date.preference.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.withtime.be.withtimebe.domain.date.preference.entity.DatePreferenceKeyword;

public interface DatePreferenceKeywordRepository extends JpaRepository<DatePreferenceKeyword, Long> {
}
