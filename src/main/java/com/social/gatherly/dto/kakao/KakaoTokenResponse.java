package com.social.gatherly.dto.kakao;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 카카오 Access Token 응답 DTO입니다.
 * AuthorizationCode 를 전송한 후에
 * 카카오가 반환하는 JSON을 저장합니다
 */

@Getter
@NoArgsConstructor
public class KakaoTokenResponse {
    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("access_token")
    private String accessToken;

    //token 기간
    @JsonProperty("expires_in")
    private  Integer expiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;

    //refresh token 기간
    @JsonProperty("refresh_token_expires_in")
    private Integer refreshTokenExpiresIn;

}
