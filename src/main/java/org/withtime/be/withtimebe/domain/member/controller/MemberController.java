package org.withtime.be.withtimebe.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.namul.api.payload.response.DefaultResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.withtime.be.withtimebe.domain.member.converter.MemberConverter;
import org.withtime.be.withtimebe.domain.member.dto.MemberRequestDTO;
import org.withtime.be.withtimebe.domain.member.dto.MemberResponseDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.service.command.MemberCommandService;
import org.withtime.be.withtimebe.domain.member.service.query.MemberQueryService;
import org.withtime.be.withtimebe.global.security.annotation.AuthenticatedMember;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@Tag(name = "사용자 API")
public class MemberController {

    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;

    @Operation(summary = "비밀번호 변경 API", description = "현재 비밀번호가 맞으면 새로운 비밀번호로 변경")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "비밀번호 변경 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - AUTH400_2: 소셜 로그인으로 가입된 사용자입니다.
                            - MEMBER400_1: 이전 비밀번호와 동일합니다.
                            """
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - AUTH401_2: 현재 비밀번호가 맞지 않습니다.
                            """
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - MEMBER404_1: 사용자를 찾지 못했습니다.
                            """
            ),
    })
    @PatchMapping("/passwords")
    public DefaultResponse<Void> changePassword(@AuthenticatedMember Member member,
                                                @RequestBody MemberRequestDTO.ChangePassword request) {
        memberCommandService.changePassword(member, request);
        return DefaultResponse.noContent();
    }

    @Operation(summary = "사용자 정보 변경 API", description = "사용자 정보 변경, 사용자를 쿠키로 인식하여 정보를 변경")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정보 변경 성공"),
            @ApiResponse(
                    responseCode = "404",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - MEMBER404_1: 사용자를 찾지 못했습니다.
                            """
            )
    })
    @PatchMapping("/infos")
    public DefaultResponse<MemberResponseDTO.ChangeInfo> changeInfo(@AuthenticatedMember Member member,
                                                         @RequestBody MemberRequestDTO.ChangeInfo request) {
        Member updatedMember = memberCommandService.changeInfo(member.getId(), request);
        return DefaultResponse.ok(MemberConverter.toChangeInfo(updatedMember));
    }

    @Operation(summary = "회원 탈퇴하기 API", description = "로그인된 토큰을 이용하여 회원 탈퇴하는 API")
    @ApiResponse(responseCode = "204", description = "회원 탈퇴 성공 (soft delete)")
    @DeleteMapping
    public DefaultResponse<Void> deleteMember(@AuthenticatedMember Member member) {
        memberCommandService.deleteMember(member.getId());
        return DefaultResponse.noContent();
    }
}
