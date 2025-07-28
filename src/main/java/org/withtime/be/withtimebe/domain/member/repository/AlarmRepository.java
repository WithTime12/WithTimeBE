package org.withtime.be.withtimebe.domain.member.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.withtime.be.withtimebe.domain.member.entity.Alarm;

import java.time.LocalDateTime;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    @Query("""
            SELECT a FROM Alarm a WHERE a.createdAt < ALL (SELECT a1.createdAt FROM Alarm a1 WHERE a1.id = :cursor)
            ORDER BY a.createdAt DESC, a.id DESC
    """)
    Slice<Alarm> findAllByCreatedAtLessThanOrderByCreatedAtDesc(@Param("cursor") Long cursor, Pageable pageable);
    Slice<Alarm> findAllByCreatedAtLessThanOrderByCreatedAtDesc(LocalDateTime createdAt, Pageable pageable);
}
