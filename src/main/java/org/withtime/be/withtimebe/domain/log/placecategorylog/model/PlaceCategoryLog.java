package org.withtime.be.withtimebe.domain.log.placecategorylog.model;

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
@Document(collection = "place_category_logs")
public class PlaceCategoryLog extends BaseEntity {

	@Id
	private String id;

	private String placeCategoryLabel;
	private LocalDate date;
	private Integer count;

	public void incrementCount(Integer count) {
		this.count += count;
	}
}
