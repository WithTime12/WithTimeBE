package org.withtime.be.withtimebe.domain.member.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Grade {

	FLIRT("Lv.1", "첫 탐색을 시작한 유저. 데이트의 세계에 발을 디딘 입문자", 0),
	EXPLORER("Lv.2", "다양한 코스를 둘러보며 취향을 탐색하는 단계", 50),
	SEEKER("Lv.3", "키워드로 나만의 데이트를 계획하고 시도해 본 유저", 150),
	NAVIGATOR("Lv.4", "코스를 저장하고, 추천도 해보며 길을 만들어가는 유저", 300),
	JOURNEYMAN("Lv.5", "리뷰나 공유를 통해 다른 커플에게 영감을 주는 활동가", 500),
	TRAILBLAZER("Lv.6", "직접 코스를 만들고, 사람들과 함께하는 데이트 리더", 800),
	TIMEKEEPER("Lv.7", "추억을 기록하고 되돌아보며 서비스에 깊이 관여", 1200),
	ROMANTIC_NOMAD("Lv.8", "계절별, 테마별 데이트를 기획해 나만의 지도 완성", 1700),
	MASTER_OF_MOMENTS("Lv.9", "진짜 ‘데이트 큐레이터’. 추천받기보다 추천하는 사람", 2300),
	WITHTIME("Lv.10", "WithTime의 세계를 완전히 정복한 전설의 사용자", 3000),
	;

	private final String level;
	private final String description;
	private final Integer requiredPoint;

	public static Grade fromPoint(int point) {
		Grade[] grades = Grade.values();
		for (int i = grades.length - 1; i >= 0; i--) {
			if (point >= grades[i].requiredPoint) {
				return grades[i];
			}
		}
		return FLIRT;
	}

	public static Integer nextRequiredPoint(int currentPoint) {
		Grade[] grades = Grade.values();
		for (int i = 0; i < grades.length - 1; i++) {
			if (currentPoint < grades[i + 1].requiredPoint && currentPoint >= grades[i].requiredPoint) {
				return grades[i + 1].requiredPoint - currentPoint;
			}
		}
		return 0;
	}
}
