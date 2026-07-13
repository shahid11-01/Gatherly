package com.social.gatherly.dto.google;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoogleUserResponse {
    private String sub;

    private String name;
    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("family_name")
    private String familyName;

    private String email;

    @JsonProperty("email_verified")
    private Boolean emailVerified;

    private String picture; //Google UserInfo API는 picture 정보를 별도의 객체가 아닌 문자열(String)로 반환한다.

    private String locale;

}
