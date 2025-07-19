package org.withtime.be.withtimebe.domain.notice.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;
import org.withtime.be.withtimebe.domain.notice.entity.enums.NoticeCategory;
import org.withtime.be.withtimebe.global.error.code.NoticeErrorCode;
import org.withtime.be.withtimebe.global.error.exception.NoticeException;

public class NoticeCategoryConverter implements Converter<String, NoticeCategory> {

	@Override
	public NoticeCategory convert(String source) {
		if(!StringUtils.hasText(source)) throw new NoticeException(NoticeErrorCode.NOTICE_CATEGORY_EMPTY);
		return NoticeCategory.findNoticeCategory(source);
	}
}

