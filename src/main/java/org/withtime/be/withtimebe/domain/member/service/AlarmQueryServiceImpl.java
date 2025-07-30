package org.withtime.be.withtimebe.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.withtime.be.withtimebe.domain.member.converter.AlarmConverter;
import org.withtime.be.withtimebe.domain.member.dto.AlarmResponseDTO;
import org.withtime.be.withtimebe.domain.member.entity.Alarm;
import org.withtime.be.withtimebe.domain.member.repository.AlarmRepository;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlarmQueryServiceImpl implements AlarmQueryService {

    private final AlarmRepository alarmRepository;

    @Override
    public AlarmResponseDTO.FindAlarmList findAlarms(Long cursor, Integer size) {
        Slice<Alarm> slice;
        Pageable pageable = PageRequest.of(0, size);
        if (cursor.equals(0L)) {
            slice = alarmRepository.findAllByCreatedAtLessThanOrderByCreatedAtDesc(LocalDateTime.now(), pageable);
        }
        else {
            slice = alarmRepository.findAllByCreatedAtLessThanOrderByCreatedAtDesc(cursor, pageable);
        }
        return AlarmConverter.toFindAlarmList(slice);
    }
}
