package com.social.gatherly.Enum;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ParticipantStatus {
    PENDING("대기 중"),
    APPROVED("승인됨"),
    REJECTED("거절됨");

    private final String description;


}
