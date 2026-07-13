package com.social.gatherly.dto.kakao;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoAccount {

    private String email;

    private KakaoProfile profile;
}
