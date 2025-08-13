package org.withtime.be.withtimebe.domain.member.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.withtime.be.withtimebe.domain.member.annotation.GetPoint;
import org.withtime.be.withtimebe.domain.member.annotation.enums.PointAction;
import org.withtime.be.withtimebe.domain.member.service.command.MemberCommandService;
import org.withtime.be.withtimebe.global.security.domain.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
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
			return;
		}
		
		// 포인트 적립
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		if(userDetails instanceof CustomUserDetails customUserDetails) {
			Long memberId = customUserDetails.getMember().getId();
			memberCommandService.addPoint(memberId, point);
		}
	}
}
