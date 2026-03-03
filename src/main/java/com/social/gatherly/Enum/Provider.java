package com.social.gatherly.Enum;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Provider {
    LOCAL("로칼"),
    GOOGLE("구글"),
    KAKAO("카카오");

    private final String description;
}
