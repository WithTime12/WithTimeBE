package org.withtime.be.withtimebe.domain.member.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.withtime.be.withtimebe.domain.member.annotation.GetPoint;
import org.withtime.be.withtimebe.domain.member.annotation.enums.PointAction;
import org.withtime.be.withtimebe.domain.member.service.command.MemberCommandService;
import org.withtime.be.withtimebe.global.security.domain.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@Order(Ordered.LOWEST_PRECEDENCE - 1) // 읽기 전용 트랜잭션보다 먼저 실행되도록 하여 포인트 변경 감지되도록
public class GetPointAspect {

	private final MemberCommandService memberCommandService;

	@AfterReturning("@annotation(getPoint)")
	public void addPoint(GetPoint getPoint) {

		// 행동 및 포인트 추출
		PointAction action = getPoint.action();
		Integer point = action.getPoint();

		// 인증 객체 추출
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication == null || !authentication.isAuthenticated()) {
			log.info("[GetPointAspect] 유저의 인증 정보가 존재하지 않아 포인트가 적립되지 않았습니다.");
			return;
		}
		
		// 포인트 적립
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		if(userDetails instanceof CustomUserDetails customUserDetails) {
			Long memberId = customUserDetails.getMember().getId();
			memberCommandService.addPoint(memberId, point);
			log.info("[GetPointAspect] {} 님의 포인트 적립에 성공하였습니다.", customUserDetails.getMember().getUsername());
		}
	}
}
