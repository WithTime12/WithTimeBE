package org.withtime.be.withtimebe.domain.date.preference.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.withtime.be.withtimebe.domain.date.preference.entity.DatePreferenceQuestion;

import java.util.List;

public interface DatePreferenceQuestionRepository extends JpaRepository<DatePreferenceQuestion, Long> {
    List<DatePreferenceQuestion> findAllByOrderByIdAsc();
}
