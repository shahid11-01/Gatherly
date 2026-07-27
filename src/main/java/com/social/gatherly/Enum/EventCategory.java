package com.social.gatherly.Enum;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventCategory {
    MUSIC("음악"),
    SPORTS("운동"),
    OUTDOOR("야외 활동"),
    TECH("기술"),
    FOOD_AND_DRINK("음식과 음료"),
    ARTS("예술"),
    GAMING("게임"),
    COOKING("요리"),
    EDUCATION("교육"),
    OTHER("기타");
    private final String description;
}
