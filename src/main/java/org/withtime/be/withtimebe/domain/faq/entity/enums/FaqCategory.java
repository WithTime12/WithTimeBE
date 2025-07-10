package org.withtime.be.withtimebe.domain.faq.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FaqCategory {
	USAGE("서비스 이용 방법"),
	ALGORITHM("추천 알고리즘 관련"),
	FEATURE("기능 및 사용성"),
	SCHEDULE("예약/일정 관리"),
	ERROR("기타/문의 오류 신고"),
	ACCOUNT("계정 및 개인정보");

	private final String label;
}