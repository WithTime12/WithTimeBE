package org.withtime.be.withtimebe.domain.member.service.query;

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

		Grade currentGrade = gradeRepository.findCurrentGrade(currentPoint)
			.orElseThrow(() -> new GradeException(GradeErrorCode.GRADE_NOT_FOUND));

		Grade nextGrade = gradeRepository.findNextGrade(currentPoint)
			.orElse(null);

		return GradeConverter.toFindMyGrade(member, currentGrade, nextGrade);
	}
}
