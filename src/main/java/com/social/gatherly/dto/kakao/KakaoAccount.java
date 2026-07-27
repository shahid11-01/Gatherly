package com.social.gatherly.dto.kakao;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KakaoAccount {

    private String email;

    @JsonProperty("profile")
    private KakaoProfile profile;
}
