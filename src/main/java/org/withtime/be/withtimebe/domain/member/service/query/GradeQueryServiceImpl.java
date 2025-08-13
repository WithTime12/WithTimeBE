package org.withtime.be.withtimebe.domain.member.service.query;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;
import org.withtime.be.withtimebe.domain.member.converter.GradeConverter;
import org.withtime.be.withtimebe.domain.member.dto.GradeResponseDTO;
import org.withtime.be.withtimebe.domain.member.entity.Grade;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.repository.GradeRepository;
import org.withtime.be.withtimebe.global.error.code.GradeErrorCode;
import org.withtime.be.withtimebe.global.error.exception.GradeException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GradeQueryServiceImpl implements GradeQueryService {

	private final GradeRepository gradeRepository;

	@Override
	public GradeResponseDTO.FindMyGrade findMyGrade(Member member) {

		int currentPoint = member.getPoint();

		List<Grade> gradeList = gradeRepository.findAll();

		// 필요 포인트 기준 오름차순 정렬
		gradeList.sort(Comparator.comparingInt(Grade::getRequiredPoint));

		// 현재 Grade
		Grade currentGrade = gradeList.stream()
			.filter(g -> g.getRequiredPoint() <= currentPoint)
			.max(Comparator.comparingInt(Grade::getRequiredPoint))
			.orElseThrow(() -> new GradeException(GradeErrorCode.GRADE_NOT_FOUND));

		// 다음 Grade
		Grade nextGrade = gradeList.stream()
			.filter(g -> g.getRequiredPoint() > currentPoint)
			.findFirst()
			.orElse(null);

		return GradeConverter.toFindMyGrade(member, currentGrade, nextGrade);
	}
}
