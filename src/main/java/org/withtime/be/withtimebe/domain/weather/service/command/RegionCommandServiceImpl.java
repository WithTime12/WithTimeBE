package org.withtime.be.withtimebe.domain.weather.service.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.withtime.be.withtimebe.domain.weather.converter.RegionConverter;
import org.withtime.be.withtimebe.domain.weather.dto.request.RegionReqDTO;
import org.withtime.be.withtimebe.domain.weather.dto.response.RegionResDTO;
import org.withtime.be.withtimebe.domain.weather.entity.Region;
import org.withtime.be.withtimebe.domain.weather.entity.RegionCode;
import org.withtime.be.withtimebe.domain.weather.repository.RegionCodeRepository;
import org.withtime.be.withtimebe.domain.weather.repository.RegionRepository;
import org.withtime.be.withtimebe.global.error.code.WeatherErrorCode;
import org.withtime.be.withtimebe.global.error.exception.WeatherException;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegionCommandServiceImpl implements RegionCommandService {

    private final WebClient weatherWebClient;

    private final RegionRepository regionRepository;
    private final RegionCodeRepository regionCodeRepository;

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.grid-conversion-url}")
    private String gridConversionUrl;

    @Override
    public RegionResDTO.CreateRegionCode createRegionCode(RegionReqDTO.CreateRegionCode request) {
        log.info("지역코드 등록 요청: {}", request.name());

        validateDuplicateRegionCode(request.landRegCode(), request.tempRegCode());

        RegionCode regionCode = RegionConverter.toRegionCode(request);
        RegionCode savedRegionCode = regionCodeRepository.save(regionCode);

        log.info("지역코드 등록 완료: {} (ID: {})", savedRegionCode.getName(), savedRegionCode.getId());
        return RegionConverter.toCreateRegionCodeResponse(savedRegionCode);
    }

    @Override
    public RegionResDTO.CreateRegion createRegion(RegionReqDTO.CreateRegion request) {
        log.info("지역 등록 요청: {}", request.name());

        // 1. 중복 체크
        validateDuplicateRegion(request.name(), request.latitude(), request.longitude());

        // 2. 지역코드 존재 확인
        RegionCode regionCode = regionCodeRepository.findById(request.regionCodeId())
                .orElseThrow(() -> new WeatherException(WeatherErrorCode.REGION_NOT_FOUND));

        // 3. 격자 좌표 변환
        CoordinateResult coordinateResult = convertToGridCoordinates(request.latitude(), request.longitude());

        // 4. 지역 저장
        Region region = RegionConverter.toRegion(request, coordinateResult.gridX(), coordinateResult.gridY(), regionCode);
        Region savedRegion = regionRepository.save(region);

        log.info("지역 등록 완료: {} (ID: {})", savedRegion.getName(), savedRegion.getId());
        return RegionConverter.toCreateRegion(savedRegion);
    }

    @Override
    public RegionResDTO.CreateRegion createRegionWithNewCode(RegionReqDTO.CreateRegionWithNewCode request) {
        log.info("지역+지역코드 등록 요청: {}", request.name());

        // 1. 중복 체크
        validateDuplicateRegion(request.name(), request.latitude(), request.longitude());
        validateDuplicateRegionCode(request.landRegCode(), request.tempRegCode());

        // 2. 격자 좌표 변환
        CoordinateResult coordinateResult = convertToGridCoordinates(request.latitude(), request.longitude());

        // 3. 지역코드 먼저 생성
        RegionCode regionCode = RegionCode.builder()
                .landRegCode(request.landRegCode())
                .tempRegCode(request.tempRegCode())
                .name(request.regionCodeName())
                .build();
        RegionCode savedRegionCode = regionCodeRepository.save(regionCode);

        // 4. 지역 저장
        Region region = RegionConverter.toEntityWithNewCode(
                request, coordinateResult.gridX(), coordinateResult.gridY(), savedRegionCode);
        Region savedRegion = regionRepository.save(region);

        log.info("지역+지역코드 등록 완료: {} (ID: {})", savedRegion.getName(), savedRegion.getId());
        return RegionConverter.toCreateRegion(savedRegion);
    }

    private void validateDuplicateRegionCode(String landRegCode, String tempRegCode) {
        if (regionCodeRepository.existsByLandRegCode(landRegCode)) {
            throw new WeatherException(WeatherErrorCode.REGION_ALREADY_EXISTS);
        }
        if (regionCodeRepository.existsByTempRegCode(tempRegCode)) {
            throw new WeatherException(WeatherErrorCode.REGION_ALREADY_EXISTS);
        }
    }

    private void validateDuplicateRegion(String name, BigDecimal latitude, BigDecimal longitude) {
        // 지역명 중복 체크
        if (regionRepository.existsByName(name)) {
            throw new WeatherException(WeatherErrorCode.REGION_ALREADY_EXISTS);
        }

        // 유사한 좌표 체크 (매우 가까운 거리의 지역이 이미 있는지 확인)
        List<Region> nearRegions = regionRepository.findByNearCoordinates(latitude, longitude);
        if (!nearRegions.isEmpty()) {
            log.warn("유사한 좌표의 지역이 이미 존재합니다: {}", nearRegions.get(0).getName());
            throw new WeatherException(WeatherErrorCode.INVALID_COORDINATES);
        }
    }

    /**
     * 기상청 API를 호출하여 위경도를 격자 좌표로 변환
     */
    private CoordinateResult convertToGridCoordinates(BigDecimal latitude, BigDecimal longitude) {
        try {
            String response = weatherWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(gridConversionUrl)
                            .queryParam("authKey", apiKey)
                            .queryParam("lat", latitude.toString())
                            .queryParam("lon", longitude.toString())
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (response == null || response.trim().isEmpty()) {
                throw new WeatherException(WeatherErrorCode.GRID_CONVERSION_ERROR);
            }

            return parseGridCoordinates(response);

        } catch (Exception e) {
            log.error("격자 좌표 변환 실패: lat={}, lon={}", latitude, longitude, e);
            throw new WeatherException(WeatherErrorCode.GRID_CONVERSION_ERROR);
        }
    }

    private CoordinateResult parseGridCoordinates(String response) {
        try {
            log.debug("파싱할 응답: {}", response);

            // 멀티라인 응답 처리를 위한 패턴 수정
            // #START7777 이후의 데이터 라인에서 숫자들을 추출
            Pattern pattern = Pattern.compile(
                    "#START7777.*?\\s+([0-9.]+),\\s*([0-9.]+),\\s*([0-9]+),\\s*([0-9]+)",
                    Pattern.DOTALL  // 개행 문자도 . 패턴에 포함
            );

            Matcher matcher = pattern.matcher(response);

            if (matcher.find()) {
                // 응답에서 추출된 값들: lon, lat, x, y 순서
                String lonStr = matcher.group(1);
                String latStr = matcher.group(2);
                String xStr = matcher.group(3);
                String yStr = matcher.group(4);

                log.debug("파싱된 값들 - LON: {}, LAT: {}, X: {}, Y: {}",
                        lonStr, latStr, xStr, yStr);

                BigDecimal x = new BigDecimal(xStr);
                BigDecimal y = new BigDecimal(yStr);

                log.debug("격자 좌표 변환 결과: X={}, Y={}", x, y);
                return new CoordinateResult(x, y);
            } else {
                // 대안 패턴: 라인별로 분리해서 처리
                String[] lines = response.split("\n");
                for (String line : lines) {
                    log.debug("처리 중인 라인: '{}'", line.trim());
                    // 숫자들이 포함된 데이터 라인 찾기
                    if (line.trim().matches("\\s*[0-9.]+,\\s*[0-9.]+,\\s*[0-9]+,\\s*[0-9]+.*")) {
                        String[] values = line.trim().split(",");
                        if (values.length >= 4) {
                            BigDecimal x = new BigDecimal(values[2].trim());
                            BigDecimal y = new BigDecimal(values[3].trim());

                            log.debug("라인별 파싱 성공 - X: {}, Y: {}", x, y);
                            return new CoordinateResult(x, y);
                        }
                    }
                }

                log.error("격자 좌표 파싱 실패: 응답 형식이 올바르지 않음. response={}", response);
                throw new WeatherException(WeatherErrorCode.API_RESPONSE_PARSING_ERROR);
            }

        } catch (NumberFormatException e) {
            log.error("숫자 변환 오류: {}", e.getMessage(), e);
            throw new WeatherException(WeatherErrorCode.API_RESPONSE_PARSING_ERROR);
        } catch (Exception e) {
            log.error("격자 좌표 파싱 중 오류 발생: {}", response, e);
            throw new WeatherException(WeatherErrorCode.API_RESPONSE_PARSING_ERROR);
        }
    }

    private record CoordinateResult(BigDecimal gridX, BigDecimal gridY) {}
}
