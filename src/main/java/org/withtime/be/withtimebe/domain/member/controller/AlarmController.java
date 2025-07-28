package org.withtime.be.withtimebe.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.namul.api.payload.response.DefaultResponse;
import org.springframework.web.bind.annotation.*;
import org.withtime.be.withtimebe.domain.member.dto.AlarmRequestDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.service.AlarmCommandService;
import org.withtime.be.withtimebe.global.security.annotation.AuthenticatedMember;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarms")
@Tag(name = "알림 테스트 API")
public class AlarmController {

    private final AlarmCommandService alarmCommandService;

    @Operation(summary = "알림 테스트용 API", description = "알림 테스트하기 위해 생성한 API")
    @ApiResponse(responseCode = "204", description = "알림 전송 성공, 해당 API는 일림 전송 실패로 따로 에러 메시지를 전송하지 않습니다.")
    @PostMapping
    public DefaultResponse<Void> alarm(@AuthenticatedMember Member member, @RequestBody AlarmRequestDTO.SendAlarm request) {
        alarmCommandService.send(member, request);
        return DefaultResponse.noContent();
    }
}
