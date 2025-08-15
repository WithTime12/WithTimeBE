package org.withtime.be.withtimebe.domain.date.controller.query;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.namul.api.payload.response.DefaultResponse;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.withtime.be.withtimebe.domain.date.converter.DateConverter;
import org.withtime.be.withtimebe.domain.date.dto.request.DateRequestDTO;
import org.withtime.be.withtimebe.domain.date.dto.response.DateResponseDTO;
import org.withtime.be.withtimebe.domain.date.entity.DateCourse;
import org.withtime.be.withtimebe.domain.date.entity.enums.*;
import org.withtime.be.withtimebe.domain.date.service.query.DateQueryService;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.global.annotation.SwaggerPageable;
import org.withtime.be.withtimebe.global.security.annotation.AuthenticatedMember;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/date-courses")
@Tag(name = "데이트 조회 API")
public class DateQueryController {

    private final DateQueryService dateQueryService;

    @Operation(summary = "데이트 코스 리스트 조회 API by 제인", description = "데이트 코스 전체 조회 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공입니다."),
            @ApiResponse(responseCode = "404", description = "DATE_COURSE404_1 : 해당하는 데이트 코스를 찾을 수 없습니다.")
    })
    @Parameters({
            @Parameter(name = "datePriceRange", in = ParameterIn.QUERY,
                    schema = @Schema(implementation = DatePriceRange.class)),
            @Parameter(name = "datePlaces", in = ParameterIn.QUERY, description = "만날 장소",
                    style = ParameterStyle.FORM, explode = Explode.TRUE,
                    array = @ArraySchema(schema = @Schema(type = "string"))),
            @Parameter(name = "dateDurationTime", in = ParameterIn.QUERY,
                    schema = @Schema(implementation = DateTime.class)),
            @Parameter(name = "mealTypes", in = ParameterIn.QUERY, description = "식사 타입",
                    style = ParameterStyle.FORM, explode = Explode.TRUE,
                    array = @ArraySchema(schema = @Schema(type = "string",allowableValues = { "BREAKFAST", "LUNCH", "DINNER" }))),
            @Parameter(name = "transportation", in = ParameterIn.QUERY,
                    schema = @Schema(implementation = Transportation.class)),
            @Parameter(name = "userPreferredKeywords", in = ParameterIn.QUERY, description = "키워드",
                    style = ParameterStyle.FORM, explode = Explode.TRUE,
                    array = @ArraySchema(schema = @Schema(type = "string")))
    })
    @SwaggerPageable
    @GetMapping
    public DefaultResponse<DateResponseDTO.DateCourseList> findDateCourses(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @ModelAttribute @ParameterObject DateRequestDTO.DateCourseSearchCond dateCourseSearchCond,
            @AuthenticatedMember Member member
            ) {
        DateResponseDTO.DateCourseList response = dateQueryService.findDateCourses(dateCourseSearchCond, pageable, member);
        return DefaultResponse.ok(response);
    }

    @Operation(summary = "데이트 코스 북마크 리스트 조회 API by 제인", description = "데이트 코스 북마크 리스트 조회 API입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공입니다"),
            @ApiResponse(responseCode = "DATE_COURSE_BOOKMARK404_1",
                    description = "해당 코스를 찾을 수 없습니다")
    })
    @Parameters({
            @Parameter(name = "datePriceRange", in = ParameterIn.QUERY,
                    schema = @Schema(implementation = DatePriceRange.class)),
            @Parameter(name = "datePlaces", in = ParameterIn.QUERY, description = "만날 장소",
                    required = false, allowEmptyValue = true,
                    style = ParameterStyle.FORM, explode = Explode.TRUE,
                    array = @ArraySchema(schema = @Schema(type = "string"))),
            @Parameter(name = "dateDurationTime", in = ParameterIn.QUERY,
                    schema = @Schema(implementation = DateTime.class)),
            @Parameter(name = "mealTypes", in = ParameterIn.QUERY, description = "식사 타입",
                    style = ParameterStyle.FORM, explode = Explode.TRUE,
                    array = @ArraySchema(schema = @Schema(implementation = MealType.class))),
            @Parameter(name = "transportation", in = ParameterIn.QUERY,
                    schema = @Schema(implementation = Transportation.class)),
            @Parameter(name = "userPreferredKeywords", in = ParameterIn.QUERY, description = "키워드",
                    style = ParameterStyle.FORM, explode = Explode.TRUE,
                    array = @ArraySchema(schema = @Schema(type = "string")))
    })
    @SwaggerPageable
    @GetMapping("/bookmarks/search")
    public DefaultResponse<DateResponseDTO.DateCourseList> findDateCourseBookmark(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @ModelAttribute @ParameterObject DateRequestDTO.DateCourseSearchCond dateCourseSearchCond,
            @AuthenticatedMember Member member
    ){
        Page<DateCourse> bookmarkedDateCourses = dateQueryService.findDateCourseBookmarks(dateCourseSearchCond, pageable, member);
        DateResponseDTO.DateCourseList response = DateConverter.createDateCourseList(bookmarkedDateCourses, null, dateCourseSearchCond);
        return DefaultResponse.ok(response);
    }
}
