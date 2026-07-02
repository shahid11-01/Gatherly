package com.social.gatherly.Dto.Kakao;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 카카오 사용자 정보를 저장하는 DTO 입니다
 * Access Token을 이용하여
 * 사용자 정보를 조회할 때 사용한다
 */
@Getter
@NoArgsConstructor
public class KakaoUserResponse {
    //키카오 유저 ID
    private Long id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

}
