package org.withtime.be.withtimebe.domain.member.service.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.withtime.be.withtimebe.domain.member.dto.MemberRequestDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;

public interface MemberCommandService {
    void changePassword(Member member, MemberRequestDTO.ChangePassword request);
    void changePassword(String email, String password);
    Member changeInfo(Long memberId, MemberRequestDTO.ChangeInfo request);
	  void addPoint(Long memberId, Integer point);
    void deleteMember(HttpServletRequest request, HttpServletResponse response, Long memberId);
}
