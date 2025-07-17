package org.withtime.be.withtimebe.domain.date.dto.response;

import java.util.List;

import lombok.Builder;

public class DatePlaceResponseDTO {

	@Builder
	public record DatePlaceManagementList(
		List<DatePlaceManagement> datePlaceManagementList,
		Integer totalPages,	// 전체 페이지 개수
		Integer currentPage,	// 현재 페이지 번호
		Integer currentSize,	// 현재 페이지의 크기
		Boolean hasNextPage	// 다음 페이지 존재 여부
	) {}

	@Builder
	public record DatePlaceManagement(
		Long datePlaceId,	// 데이트 장소 식별자 값
		String name,	// 장소 이름
		String tel,		// 전화 번호
		Integer averagePrice,	// 평균 가격
		String lotNumberAddress,	// 지번 주소
		String placeType,	// 장소 유형
		List<PlaceCategory> placeCategoryList	// 카테고리 목록
	) {}

	@Builder
	public record PlaceCategory(
		Long placeCategoryId,
		String placeCategoryType,
		String label
	) {}
}
