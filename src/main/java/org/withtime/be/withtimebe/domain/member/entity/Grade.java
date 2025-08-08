package org.withtime.be.withtimebe.domain.member.entity;

import org.withtime.be.withtimebe.domain.member.entity.enums.GradeType;
import org.withtime.be.withtimebe.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "grade")
public class Grade extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "grade_id")
	private Long id;

	@Enumerated(EnumType.STRING)g
	@Column(nullable = false, unique = true)
	private GradeType gradeType;

	@Column(nullable = false)
	private String level;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private Integer requiredPoint;
}
