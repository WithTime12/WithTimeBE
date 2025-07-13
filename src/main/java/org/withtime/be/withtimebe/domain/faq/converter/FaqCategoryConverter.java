package org.withtime.be.withtimebe.domain.faq.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;
import org.withtime.be.withtimebe.domain.faq.entity.enums.FaqCategory;
import org.withtime.be.withtimebe.global.error.code.FaqErrorCode;
import org.withtime.be.withtimebe.global.error.exception.FaqException;

// @PathVariable, @RequestParam
public class FaqCategoryConverter implements Converter<String, FaqCategory> {

	@Override
	public FaqCategory convert(String source) {
		if(StringUtils.hasText(source)) throw new FaqException(FaqErrorCode.FAQ_CATEGORY_EMPTY);
		return FaqCategory.findFaqCategory(source);
	}
}