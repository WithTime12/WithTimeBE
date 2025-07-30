package org.withtime.be.withtimebe.domain.log.datecourselog.service.query;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.withtime.be.withtimebe.domain.date.repository.DateCourseRepository;
import org.withtime.be.withtimebe.domain.date.repository.DatePlaceDateCourseRepository;
import org.withtime.be.withtimebe.domain.log.datecourselog.converter.DateCourseLogConverter;
import org.withtime.be.withtimebe.domain.log.datecourselog.dto.DateCourseLogResponseDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DateCourseLogQueryServiceImpl implements DateCourseLogQueryService {

	private final DateCourseRepository dateCourseRepository;
	private final MemberRepository memberRepository;
	private final DatePlaceDateCourseRepository datePlaceDateCourseRepository;

	@Override
	public DateCourseLogResponseDTO.FindAverageDateCourseCount findAverageDateCourseCount(Member member) {

		LocalDate now = LocalDate.now();
		LocalDate oneMonthAgo = now.minusDays(30);

		// 최근 1개월동안 생성된 데이트 코스
		Long dateCourseCount = dateCourseRepository.countByCreatedAtBetween(oneMonthAgo, now);

		// 전체 멤버 수
		Long memberCount = memberRepository.count();

		// 평균 데이트 횟수
		Double averageDateCount = (double) dateCourseCount / memberCount;
		averageDateCount = Math.round(averageDateCount * 10.0) / 10.0;	// 첫째 자리까지

		// 나의 데이트 횟수
		Long myDateCount = (member == null) ? 0L : dateCourseRepository.countByMemberId(member.getId());

		return DateCourseLogConverter.toFindAverageDateCourseCount(averageDateCount, myDateCount);
	}

	@Override
	public DateCourseLogResponseDTO.FindSavedDateCourseCount findSavedDateCourseCount(Member member) {
		Long savedCount = (member == null) ? 0L : datePlaceDateCourseRepository.countByCreatorMemberId(member.getId());
		return DateCourseLogConverter.toFindSavedDateCourseCount(savedCount);
	}
}
