package org.withtime.be.withtimebe.domain.member.annotation.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PointAction {

	KEYWORD_SEARCH("키워드 검색", 3),
	VIEW_DATE_COURSE("데이트 코스 조회", 5),
	SAVE_DATE_COURSE("데이트 코스 저장", 10),
	CREATE_DATE_COURSE("데이트 코스 생성", 30),
	WRITE_REVIEW("리뷰 작성", 20),
	COMPLETE_TEST("취향 테스트 완료", 20),
	INPUT_PROFILE("프로필 입력", 10),
	// COMPLETE_MISSION("미션 완료", 0),
	;

	private final String label;
	private final Integer point;
}