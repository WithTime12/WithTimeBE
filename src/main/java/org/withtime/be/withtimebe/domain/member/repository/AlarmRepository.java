package org.withtime.be.withtimebe.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.withtime.be.withtimebe.domain.member.entity.Alarm;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
}
