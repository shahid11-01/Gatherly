package com.social.gatherly.Dto.Kakao;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoAccount {

    private String email;

    private KakaoProfile profile;
}
