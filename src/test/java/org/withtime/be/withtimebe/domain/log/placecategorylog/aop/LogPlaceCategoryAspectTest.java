package org.withtime.be.withtimebe.domain.log.placecategorylog.aop;

import static org.mockito.BDDMockito.*;

import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

@ExtendWith(MockitoExtension.class)
@DisplayName("[AOP] LogPlaceCategoryAspect 단위 테스트")
class LogPlaceCategoryAspectTest {

	@InjectMocks
	private LogPlaceCategoryAspect aspect;

	@Mock
	private RedisTemplate<String, Object> redisTemplate;

	@Mock
	private ZSetOperations<String, Object> zSetOperations;

	// JoinPoint 생성 메서드
	private JoinPoint createJoinPoint(String[] paramNames, Object[] args) {
		JoinPoint joinPoint = mock(JoinPoint.class);
		MethodSignature methodSignature = mock(MethodSignature.class);

		given(joinPoint.getSignature()).willReturn(methodSignature);
		given(joinPoint.getArgs()).willReturn(args);
		given(methodSignature.getParameterNames()).willReturn(paramNames);

		return joinPoint;
	}

	@Nested
	@DisplayName("logPlaceCategory()")
	class LogPlaceCategoryTest {

		@Test
		@DisplayName("'placeCategoryId'가 단일 인자로 들어오면, Redis에 ZSET으로 기록된다.")
		void single_placeCategoryId_param_test() {
			// given
			Long expectedId = 1L;
			Integer expectedMethodCall = 1;

			JoinPoint joinPoint = createJoinPoint(
				new String[] {"placeCategoryId", "anotherId", "anotherDTO"},
				new Object[] {expectedId, 1L, new Object()}
			);

			given(redisTemplate.opsForZSet()).willReturn(zSetOperations);
			given(redisTemplate.getExpire(anyString(), any())).willReturn(-1L);

			// when
			aspect.logPlaceCategory(joinPoint);

			// then
			verify(zSetOperations, times(expectedMethodCall)).incrementScore(anyString(), eq(expectedId), eq(1.0));
			verify(redisTemplate, times(expectedMethodCall)).expire(anyString(), anyLong(), any());
		}

		@Test
		@DisplayName("'placeCategoryId'가 List로 들어오면, Redis에 ZSET으로 기록된다.")
		void list_placeCategoryId_param_test() {
			// given
			List<Long> expectedIdList = List.of(1L, 2L, 3L);
			Integer expectedMethodCall = 1;

			JoinPoint joinPoint = createJoinPoint(
				new String[] {"placeCategoryIdList", "anotherDTO", "anotherIdList"},
				new Object[] {expectedIdList, new Object(), List.of(1L, 2L)}
			);

			given(redisTemplate.opsForZSet()).willReturn(zSetOperations);
			given(redisTemplate.getExpire(anyString(), any())).willReturn(-1L);

			// when
			aspect.logPlaceCategory(joinPoint);

			// then
			expectedIdList
				.forEach(id -> verify(zSetOperations, times(expectedMethodCall)).incrementScore(anyString(), eq(id), eq(1.0)));
			verify(redisTemplate, times(expectedMethodCall)).expire(anyString(), anyLong(), any());
		}

		@Test
		@DisplayName("'placeCategoryId'가 DTO 내부 필드에 있으면 Redis에 기록된다.")
		void dto_placeCategoryId_field_test() {
			// given
			Long expectedId = 1L;
			Integer expectedMethodCall = 1;

			class RequestDTO {
				Long placeCategoryId = expectedId;
			}
			class AnotherDTO {
				Long anotherId = 1L;
			}
			Object requestDTO = new RequestDTO();
			Object anotherDTO = new AnotherDTO();

			JoinPoint joinPoint = createJoinPoint(
				new String[] {"requestDTO", "anotherDTO"},
				new Object[] {requestDTO, anotherDTO}
			);

			given(redisTemplate.opsForZSet()).willReturn(zSetOperations);
			given(redisTemplate.getExpire(anyString(), any())).willReturn(-1L);

			// when
			aspect.logPlaceCategory(joinPoint);

			// then
			verify(zSetOperations, times(expectedMethodCall)).incrementScore(anyString(), eq(expectedId), eq(1.0));
			verify(redisTemplate, times(expectedMethodCall)).expire(anyString(), anyLong(), any());
		}

		@Test
		@DisplayName("'placeCategoryIdList'가 DTO 내부 List 필드로 있으면, Redis에 각각 기록된다.")
		void dto_placeCategoryId_list_test() {
			// given
			List<Long> expectedIdList = List.of(1L, 2L, 3L);
			Integer expectedMethodCall = 1;

			class RequestDTO {
				List<Long> placeCategoryIdList = expectedIdList;
			}
			class AnotherDTO {
				List<Long> anotherIdList = expectedIdList;
			}
			Object requestDTO = new RequestDTO();
			Object anotherDTO = new AnotherDTO();

			JoinPoint joinPoint = createJoinPoint(
				new String[] {"requestDTO", "anotherDTO", "anotherId"},
				new Object[] {requestDTO, anotherDTO, 1L}
			);

			given(redisTemplate.opsForZSet()).willReturn(zSetOperations);
			given(redisTemplate.getExpire(anyString(), any())).willReturn(-1L);

			// when
			aspect.logPlaceCategory(joinPoint);

			// then
			expectedIdList.forEach(id ->
				verify(zSetOperations, times(expectedMethodCall)).incrementScore(anyString(), eq(id), eq(1.0))
			);
			verify(redisTemplate, times(expectedMethodCall)).expire(anyString(), anyLong(), any());
		}
	}
}