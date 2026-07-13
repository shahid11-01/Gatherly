package com.social.gatherly.dto.google;



/*
google Access Token 응답입니다
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoogleTokenResponse {
    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Integer expiresIn;  //구글 엑세스 토큰 만료 기간

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("id_token")
    private String idToken;  //openID Connect에서 제공하는 JWT

    @JsonProperty("scope")
    private String scope;  //구글 api 권한 범위(profile, email)

}
