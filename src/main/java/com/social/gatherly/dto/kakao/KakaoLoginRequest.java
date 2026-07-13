package com.social.gatherly.dto.kakao;


import lombok.Getter;
import lombok.Setter;

/**
 * React Native에서 전달되는
 * Authorization Code DTO입니다
 */

@Getter
@Setter
public class KakaoLoginRequest {
    private String code;
    ;
}
