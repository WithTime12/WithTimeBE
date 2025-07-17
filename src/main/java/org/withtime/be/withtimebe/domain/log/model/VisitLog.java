package org.withtime.be.withtimebe.domain.log.model;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.withtime.be.withtimebe.global.common.BaseEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Document(collection = "visit_logs")
public class VisitLog extends BaseEntity {

	@Id
	private String id;

	private LocalDate date;

	private LocalTime hour;

	private Long count;
}
