package org.withtime.be.withtimebe.domain.log.dateplacelog.entity;

import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.Document;
import org.withtime.be.withtimebe.global.common.BaseEntity;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Document(collection = "date_place_logs")
public class DatePlaceLog extends BaseEntity {

	@Id
	private String id;

	private LocalDate date;
	private Long count;
}
