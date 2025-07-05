package org.withtime.be.withtimebe.domain.date.entity.enums;

public enum Day {

    MON("월"),
    TUE("화"),
    WED("수"),
    THU("목"),
    FRI("금"),
    SAT("토"),
    SUN("일");

    private final String label;

    Day(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static Day fromLabel(String label) {
        for (Day day : values()) {
            if (day.label.equals(label)) {
                return day;
            }
        }
        // 차후 공통 익셉션으로 수정
        throw new IllegalArgumentException("해당 라벨로 요일을 찾을 수 없습니다: " + label);
    }
}