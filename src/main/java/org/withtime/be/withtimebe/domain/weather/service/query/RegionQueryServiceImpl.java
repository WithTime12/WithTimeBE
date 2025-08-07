package org.withtime.be.withtimebe.domain.weather.service.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.repository.MemberRepository;
import org.withtime.be.withtimebe.domain.weather.converter.RegionConverter;
import org.withtime.be.withtimebe.domain.weather.dto.response.RegionResDTO;
import org.withtime.be.withtimebe.domain.weather.entity.Region;
import org.withtime.be.withtimebe.domain.weather.repository.RegionCodeRepository;
import org.withtime.be.withtimebe.domain.weather.repository.RegionRepository;
import org.withtime.be.withtimebe.global.error.code.MemberErrorCode;
import org.withtime.be.withtimebe.global.error.code.RegionErrorCode;
import org.withtime.be.withtimebe.global.error.code.WeatherErrorCode;
import org.withtime.be.withtimebe.global.error.exception.MemberException;
import org.withtime.be.withtimebe.global.error.exception.RegionException;
import org.withtime.be.withtimebe.global.error.exception.WeatherException;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegionQueryServiceImpl implements RegionQueryService{

    private final RegionCodeRepository regionCodeRepository;
    private final RegionRepository regionRepository;
    private final MemberRepository memberRepository;

    private static final Long DEFAULT_REGION_ID = 1L;

    @Override
    public RegionResDTO.RegionCodeList getAllRegionCodes() {
        List<Object[]> regionCodesWithCount = regionCodeRepository.findAllWithRegionCount();
        return RegionConverter.toRegionCodeList(regionCodesWithCount);
    }

    @Override
    public RegionResDTO.RegionList getAllRegions() {
        List<Region> regions = regionRepository.findAllActiveRegions();
        return RegionConverter.toRegionList(regions);
    }

    @Override
    public RegionResDTO.RegionInfo getRegionById(Long regionId) {
        Region region = regionRepository.findByIdWithRegionCode(regionId)
                .orElseThrow(() -> new WeatherException(WeatherErrorCode.REGION_NOT_FOUND));
        return RegionConverter.toRegionInfo(region);
    }

    @Override
    public RegionResDTO.RegionSearchResult searchRegions(String keyword) {
        List<Region> regions = regionRepository.searchByNameContaining(keyword);
        return RegionConverter.toSearchResult(regions, keyword);
    }

    @Override
    @Transactional
    public RegionResDTO.UserRegion getCurrentUserRegion(Member member) {
        // 현재 로그인한 사용자 조회
        Member currentMember = memberRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        Region userRegion = currentMember.getRegion();

        // 사용자에게 지역이 설정되지 않은 경우 기본 지역 반환
        if (userRegion == null) {
            userRegion = regionRepository.findByIdWithRegionCode(DEFAULT_REGION_ID)
                    .orElseThrow(() -> new RegionException(RegionErrorCode.REGION_NOT_FOUND));

            currentMember.updateRegion(userRegion);
            memberRepository.save(currentMember);

            log.info("사용자 {}에게 기본 지역({})이 자동 설정되었습니다.",
                    currentMember.getUsername(), userRegion.getName());
        }

        return RegionConverter.toUserRegion(userRegion);
    }
}
