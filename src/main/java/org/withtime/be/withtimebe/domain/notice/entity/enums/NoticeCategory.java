package org.withtime.be.withtimebe.domain.notice.entity.enums;

import java.util.Arrays;

import org.withtime.be.withtimebe.global.error.code.NoticeErrorCode;
import org.withtime.be.withtimebe.global.error.exception.NoticeException;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum NoticeCategory {
    SERVICE,
    SYSTEM;

    // @RequestBody
    @JsonCreator
    public static NoticeCategory findNoticeCategory(String label) {
        return Arrays.stream(values())
            .filter(type -> type.name().equalsIgnoreCase(label))
            .findAny()
            .orElseThrow(
                () -> new NoticeException(NoticeErrorCode.NOTICE_CATEGORY_NOT_FOUND)
            );
    }
}
