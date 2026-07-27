package com.social.gatherly.dto.kakao;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KakaoProfile {
    private String nickname;

    @JsonProperty("profile_image_url")
    private String profileImageUrl;
}
