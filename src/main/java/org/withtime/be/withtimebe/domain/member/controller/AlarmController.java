package org.withtime.be.withtimebe.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.namul.api.payload.response.DefaultResponse;
import org.springframework.web.bind.annotation.*;
import org.withtime.be.withtimebe.domain.member.converter.AlarmConverter;
import org.withtime.be.withtimebe.domain.member.dto.AlarmRequestDTO;
import org.withtime.be.withtimebe.domain.member.dto.AlarmResponseDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.service.AlarmCommandService;
import org.withtime.be.withtimebe.global.security.annotation.AuthenticatedMember;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarms")
@Tag(name = "알림 API")
public class AlarmController {

    private final AlarmCommandService alarmCommandService;

    @Operation(summary = "알림 테스트용 API", description = "알림 테스트하기 위해 생성한 API")
    @ApiResponse(responseCode = "204", description = "알림 전송 성공, 해당 API는 일림 전송 실패로 따로 에러 메시지를 전송하지 않습니다.")
    @PostMapping
    public DefaultResponse<Void> alarm(@AuthenticatedMember Member member, @RequestBody AlarmRequestDTO.SendAlarm request) {
        alarmCommandService.send(member, request);
        return DefaultResponse.noContent();
    }

    @Operation(summary = "푸시알림 기기 업데이트 API", description = "푸시 알림 받을 기기에서 얻은 토큰을 적용하여 해당 기기로 받도록 하는 API")
    @ApiResponse(responseCode = "204", description = "알림 받을 기기 변경에 성공했습니다.")
    @PostMapping("/device-tokens")
    public DefaultResponse<Void> updateDeviceToken(@AuthenticatedMember Member member, @RequestBody AlarmRequestDTO.UpdateDeviceToken request) {
        alarmCommandService.updateDeviceToken(member, request);
        return DefaultResponse.noContent();
    }

    @Operation(summary = "알림 설정 업데이트 API", description = "사용자의 알림 설정을 변경하는 API")
    @ApiResponse(responseCode = "200", description = "알림 설정 변경에 성공하였습니다.")
    @PatchMapping("/settings")
    public DefaultResponse<AlarmResponseDTO.UpdateSetting> updateAlarmSetting(@AuthenticatedMember Member member, @RequestBody AlarmRequestDTO.UpdateSetting request) {
        AlarmResponseDTO.UpdateSetting response = alarmCommandService.updateAlarmSetting(member, request);
        return DefaultResponse.ok(response);
    }

    @Operation(summary = "알림 설정 조회 API", description = "사용자 알림 설정 상태를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "알림 설정 조회에 성공하였습니다.")
    @GetMapping("/settings")
    public DefaultResponse<AlarmResponseDTO.SettingInfo> findSettingInfo(@AuthenticatedMember Member member) {
        return DefaultResponse.ok(AlarmConverter.toSettingInfo(member));
    }

}
