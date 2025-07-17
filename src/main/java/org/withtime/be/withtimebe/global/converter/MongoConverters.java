package org.withtime.be.withtimebe.global.converter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
public class MongoConverters {

	@WritingConverter
	public static class LocalDateToDateKstConverter implements Converter<LocalDate, Date> {
		@Override
		public Date convert(LocalDate source) {
			return Timestamp.valueOf(source.atStartOfDay().plusHours(9));
		}
	}

	@WritingConverter
	public static class LocalTimeToDateKstConverter implements Converter<LocalTime, Date> {
		@Override
		public Date convert(LocalTime source) {
			LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), source).plusHours(9);
			return Timestamp.valueOf(localDateTime);
		}
	}

	@ReadingConverter
	public static class DateToLocalDateKstConverter implements Converter<Date, LocalDate> {
		@Override
		public LocalDate convert(Date source) {
			return source.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime()
				.minusHours(9)
				.toLocalDate();
		}
	}

	@ReadingConverter
	public static class DateToLocalTimeKstConverter implements Converter<Date, LocalTime> {

		@Override
		public LocalTime convert(Date source) {
			return source.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime()
				.minusHours(9)
				.toLocalTime();
		}
	}
}
