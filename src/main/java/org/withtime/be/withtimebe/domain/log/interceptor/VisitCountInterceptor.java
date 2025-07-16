package org.withtime.be.withtimebe.domain.log.interceptor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class VisitCountInterceptor implements HandlerInterceptor {

	private final RedisTemplate<String, String> redisTemplate;

	// 리버스 프록시 확장 고려
	private static final String[] PROXY_HEADER_NAMES = {
		"X-Forwarded-For",
		"Proxy-Client-IP",
		"WL-Proxy-Client-IP",
		"HTTP_CLIENT_IP",
		"HTTP_X_FORWARDED_FOR"
	};

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		// IP 기반 방문자 기록
		String clientIp = getClientIp(request);
		LocalDate today = LocalDate.now();
		Integer hour = LocalTime.now().getHour();

		// Key
		String redisKey = String.format("visitCount:%s:%02d", today.toString(), hour);

		// Set
		redisTemplate.opsForSet().add(redisKey, clientIp);

		// TTL
		Long expire = redisTemplate.getExpire(redisKey);
		if (expire < 0) {
			redisTemplate.expire(redisKey, Duration.ofDays(1));
		}

		return true;
	}

	private String getClientIp(HttpServletRequest request) {
		for (String header : PROXY_HEADER_NAMES) {
			String ip = request.getHeader(header);
			if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
				return ip.split(",")[0].trim();
			}
		}
		return request.getRemoteAddr();
	}
}
