package org.withtime.be.withtimebe.domain.log.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import org.withtime.be.withtimebe.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "visit_log")
public class VisitLog extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "visit_log_id")
	private Long id;

	@Column(name = "date", nullable = false)
	private LocalDate date;

	@Column(name = "hour", nullable = false)
	private LocalTime hour;

	@Column(name = "count", nullable = false)
	private Long count;
}
