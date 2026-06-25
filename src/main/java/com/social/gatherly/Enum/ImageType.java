package com.social.gatherly.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageType {
    PROFILE("profile"),
    EVENT("event")
    ;
    private final String type;
}
