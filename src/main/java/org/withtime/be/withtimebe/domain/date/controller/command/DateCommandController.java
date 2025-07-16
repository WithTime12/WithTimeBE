package org.withtime.be.withtimebe.domain.date.controller.command;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.namul.api.payload.response.DefaultResponse;
import org.springframework.web.bind.annotation.*;
import org.withtime.be.withtimebe.domain.date.converter.DateConverter;
import org.withtime.be.withtimebe.domain.date.dto.response.DateResponseDTO;
import org.withtime.be.withtimebe.domain.date.entity.DateCourseBookmark;
import org.withtime.be.withtimebe.domain.date.service.command.DateCommandService;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.global.security.annotation.AuthenticatedMember;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/date")
public class DateCommandController {

    private final DateCommandService dateCommandService;

    @Operation(summary = "데이트 코스 북마크 생성 API by 제인", description = "데이트 코스 북마크 API입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공입니다"),
            @ApiResponse(responseCode = "DATE_COURSE404_1",
                    description = "해당 코스를 찾을 수 없습니다")
    })
    @PostMapping("/bookmark/{dateCourseId}")
    public DefaultResponse<DateResponseDTO.DateCourseBookmark> createDateCourseBookmark(
            @PathVariable Long dateCourseId,
            @AuthenticatedMember Member member
    ){
        DateCourseBookmark dateCourseBookmark = dateCommandService.createDateCourseBookmark(dateCourseId, member);
        DateResponseDTO.DateCourseBookmark responseDTO = DateConverter.createDateCourseBookmarkResponseDTO(dateCourseBookmark);
        return DefaultResponse.created(responseDTO);
    }

    @Operation(summary = "데이트 코스 북마크 삭제 API by 제인", description = "데이트 코스 북마크 API입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공입니다"),
            @ApiResponse(responseCode = "DATE_COURSE404_1",
                    description = "해당 코스를 찾을 수 없습니다")
    })
    @DeleteMapping("/bookmark/{dateCourseId}")
    public DefaultResponse<String> deleteDateCourseBookmark(
            @PathVariable Long dateCourseId,
            @AuthenticatedMember Member member
    ){
        dateCommandService.deleteDateCourseBookmark(dateCourseId, member);
        return DefaultResponse.noContent();
    }
}
