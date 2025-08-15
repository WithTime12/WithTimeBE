
# WithTime Backend

> 사용자의 예산과 취향에 맞춰 **맞춤형 데이트 코스**를 추천·공유하는 플랫폼의 백엔드 서버입니다.  
> Spring Boot 기반 REST API 서버로, AWS 인프라와 Redis 캐싱, JPA 최적화를 통해 안정적이고 빠른 서비스를 제공합니다.

---

## 📝 서비스 소개
**WithTime**은 사용자가 원하는 예산과 본인의 취향을 입력하면,  
이에 맞춰 데이트 코스를 자동으로 구성하여 추천해주는 서비스입니다.  
추가로 날씨 기반 추천, 인기 키워드, 사용자 등급, 알림, 취향 테스트 등 다양한 기능을 제공합니다.

---

## 🛠 기술 스택

### Backend
- **Java 17**
- **Spring Boot 3.x**
- Spring Data JPA, Spring Security, Validation
- Swagger (Springdoc OpenAPI 3)
- JWT 기반 인증/인가

### Database & Cache
- **MySQL 8** (Amazon RDS)
- **Redis (AWS ElastiCache)** – 캐시, 세션, 추천 데이터 임시 저장

### Infra & DevOps
- **AWS EC2, S3, Route53, ElastiCache, RDS**
- Docker / Docker Compose
- GitHub Actions (CI/CD)
- CloudWatch 모니터링

---

## 📌 주요 기능

### 1. 데이트 코스
- 맞춤형 데이트 코스 생성 API
- 코스 북마크 생성/삭제/조회
- 코스 목록 조회 및 검색

### 2. 데이트 취향 테스트
- 취향 테스트 진행 및 결과 조회
- 잘 맞는 유형/안 맞는 유형 분석
- 취향 데이터 초기화

### 3. 날씨 기반 추천
- 지역별 주간 날씨 기반 추천
- 강수확률 기반 추천
- 관리자 수동 동기화 트리거

### 4. 사용자 관리
- 회원가입 / 로그인 / 로그아웃
- 소셜 로그인 (OAuth2)
- 사용자 정보 수정/삭제
- 비밀번호 변경, 이메일 인증
- 회원 등급 조회

### 5. 공지사항 & FAQ
- 공지사항 CRUD (관리자 전용)
- 자주 묻는 질문 CRUD (관리자 전용)
- 검색 및 상세 조회

### 6. 통계
- 주간 인기 키워드
- 월별 데이트 장소 등록 수
- 내 데이트 횟수 vs 평균 횟수

### 7. 알림
- 푸시 알림 발송 및 설정
- 기기 토큰 업데이트
- 알림 리스트 조회

---

## 🗂 API 문서
Swagger UI: [https://api.withtime.cloud/swagger-ui/index.html](https://api.withtime.cloud/swagger-ui/index.html)  
OpenAPI Spec: [https://api.withtime.cloud/v3/api-docs](https://api.withtime.cloud/v3/api-docs)

---

## 📡 아키텍처

---

# 📋 Commit Message Convention (이모지 + 소문자 적용)
| Tag         | Description             |
|------------|-------------------------|
| `✨ feat`    | 새로운 기능 추가        |
| `🐛 fix`     | 버그 수정              |
| `📝 docs`    | 문서 추가, 수정, 삭제  |
| `🧪 test`    | 테스트 코드 추가, 수정, 삭제 |
| `🎨 style`   | 코드 형식 변경         |
| `♻️ refactor` | 코드 리팩토링         |
| `⚡ perf`    | 성능 개선              |
| `🏗️ build`  | 빌드 관련 변경사항     |
| `⚙️ ci`      | CI 관련 설정 수정     |
| `🚀 chore`   | 기타 변경사항          |
