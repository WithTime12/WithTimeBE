package org.withtime.be.withtimebe.domain.dateplace.converter;

import java.util.List;

import org.springframework.data.domain.Page;
import org.withtime.be.withtimebe.domain.date.entity.PlaceCategory;
import org.withtime.be.withtimebe.domain.dateplace.dto.response.DatePlaceResponseDTO;
import org.withtime.be.withtimebe.domain.dateplace.entity.DatePlace;

public class DatePlaceConverter {

	public static DatePlaceResponseDTO.DatePlaceManagementList toDatePlaceManagementList(Page<DatePlace> datePlacePage) {

		List<DatePlaceResponseDTO.DatePlaceManagement> datePlaceManagementList = datePlacePage.stream()
			.map(DatePlaceConverter::toDatePlaceManagement)
			.toList();

		return DatePlaceResponseDTO.DatePlaceManagementList.builder()
			.datePlaceManagementList(datePlaceManagementList)
			.totalPages(datePlacePage.getTotalPages())
			.currentPage(datePlacePage.getNumber())
			.currentSize(datePlacePage.getNumberOfElements())
			.hasNextPage(datePlacePage.hasNext())
			.build();
	}

	public static DatePlaceResponseDTO.DatePlaceManagement toDatePlaceManagement(DatePlace datePlace) {

		List<DatePlaceResponseDTO.PlaceCategory> placeCategoryList = datePlace.getDatePlacePlaceCategoryList().stream()
			.map(datePlacePlaceCategory -> toPlaceCategory(datePlacePlaceCategory.getPlaceCategory()))
			.toList();

		return DatePlaceResponseDTO.DatePlaceManagement.builder()
			.datePlaceId(datePlace.getId())
			.name(datePlace.getName())
			.tel(datePlace.getTel())
			.averagePrice(datePlace.getAveragePrice())
			.lotNumberAddress(datePlace.getLotNumberAddress())
			.placeType(datePlace.getPlaceType().getLabel())
			.placeCategoryList(placeCategoryList)
			.build();
	}

	public static DatePlaceResponseDTO.PlaceCategory toPlaceCategory(PlaceCategory placeCategory) {

		return DatePlaceResponseDTO.PlaceCategory.builder()
			.placeCategoryId(placeCategory.getId())
			.placeCategoryType(placeCategory.getCategoryType().name())
			.label(placeCategory.getLabel())
			.build();
	}
}
