package org.withtime.be.withtimebe.domain.date.entity.enums;

public enum PlaceCategoryType {

    // 분위기
    MOOD_ROMANTIC("로맨틱한", "연인과의 감성적인 분위기, 기념일, 야경 등"),
    MOOD_WARM("감성적인", "따뜻하고 정서적인 공간, 잔잔한 대화에 어울림"),
    MOOD_SERENE("조용한", "북카페, 전시, 자연 등 조용히 보내는 데이트"),
    MOOD_CHILL("잔잔한", "부드럽고 편안한 분위기의 하루"),
    MOOD_LIVELY("활기찬", "에너지 있고 북적이는 공간, 사람 구경 중심"),
    MOOD_VIBRANT("감각적인", "트렌디하고 세련된 분위기의 장소"),
    MOOD_DARK("어두운 조명", "조명 낮고 분위기 있는 공간 (바, 이자카야 등)"),
    MOOD_PLAYFUL("유쾌한", "밝고 편한 분위기, 웃음 많은 데이트"),
    MOOD_NOSTALGIC("레트로 감성", "복고풍 공간, 골목 탐방 중심"),
    MOOD_COZY("아늑한", "작고 조용한 공간, 실내 중심 소규모 장소"),
    MOOD_ANNIVERSARY("기념일 데이트", "특별한 날을 위한 코스 추천"),
    MOOD_FIRST("첫 데이트 추천", "부담 적고 무난한 코스 구성"),

    // 활동량
    ACT_WALK("산책 중심", "가볍게 걷는 동선이 포함된 데이트"),
    ACT_CHILL("편하게 쉬기", "오래 앉아 대화 중심, 카페 중심"),
    ACT_MOVE("활동적인", "많이 걷고 체험하는 에너지 있는 구성"),
    ACT_EXPLORE("탐험 중심", "새로운 장소를 여유 있게 탐방"),
    ACT_PHOTO("사진 중심", "포토존이 많고 시각적으로 즐거운 장소"),
    ACT_STAYSHORT("짧은 일정", "1~2시간 이내 짧은 데이트"),
    ACT_LONGSTAY("긴 일정", "반나절~하루 일정의 여유 있는 구성"),
    ACT_PAIR("같이 하는 체험", "두 사람이 함께 만드는 체험 (수공예 등)"),

    // 장소 스타일
    PLACE_ROOFTOP("루프탑 카페", "고층에서 도시 뷰를 볼 수 있는 공간"),
    PLACE_VIEW("전망 좋은 곳", "시야가 트인 풍경, 시티뷰 등"),
    PLACE_RIVER("한강 뷰", "수변/강 근처 코스"),
    PLACE_MARKET("로컬 마켓", "플리마켓, 편집숍 구경"),
    PLACE_NATURE("자연 가까운 곳", "숲, 강변, 공원 등 자연 동선 포함"),
    PLACE_EXHIBIT("전시 공간", "미술관, 팝업 전시 등 문화적 체험 공간"),
    PLACE_CRAFT("수공예 체험 공간", "만들기 중심 체험 장소"),
    PLACE_THEATER("소극장 공연", "뮤지컬, 연극, 콘서트 등"),
    PLACE_LIBRARY("북카페/책방", "조용한 분위기의 공간"),
    PLACE_IZAKAYA("이자카야/펍", "조명 낮고 술과 식사를 함께할 수 있는 공간"),
    PLACE_FUSION("퓨전 음식점", "이색적인 메뉴 중심 식당"),
    PLACE_BRUNCH("브런치 카페", "오전/낮 중심 여유 있는 식사 장소"),
    PLACE_DESSERT("디저트 카페", "음료+디저트 중심 공간"),
    PLACE_KOREAN("한식 오마카세", "고급스러운 한국 식사 공간"),
    PLACE_VINTAGE("레트로 골목", "복고풍 상점, 건물, 거리"),
    PLACE_MALL("복합 공간 (몰)", "쇼핑, 카페, 산책 등 한 곳에서 가능한 공간"),

    // 상황 & 시간대 & 날씨
    TIME_BRUNCH("브런치 데이트", "오전 데이트 중심"),
    TIME_SUNSET("노을 보기 좋은", "5~7시 중심 타이밍"),
    TIME_EVENING("저녁 데이트", "저녁 식사 중심 또는 조명 활용 코스"),
    WEATHER_HOT("더운 날 추천", "실내 위주 동선 제안"),
    WEATHER_RAINY("비 오는 날 추천", "실내만으로 구성되는 코스"),
    WEATHER_COLD("추운 날 추천", "따뜻하고 실내 위주 공간");

    private final String label;
    private final String detail;

    PlaceCategoryType(String label, String detail) {
        this.label = label;
        this.detail = detail;
    }

    public String getLabel() {
        return label;
    }

    public String getDetail() {
        return detail;
    }

    public static PlaceCategoryType fromLabel(String label) {
        for (PlaceCategoryType type : values()) {
            if (type.label.equals(label)) {
                return type;
            }
        }
        // 차후 공통 익셉션으로 처리
        throw new IllegalArgumentException("해당 라벨로 태그를 찾을 수 없습니다: " + label);
    }
}
