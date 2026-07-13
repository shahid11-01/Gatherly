package com.social.gatherly.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "kakao")
public class KakaoProperties {

    private String clientId;

    private String clientSecret;

    private String redirectUri;

    //Access Token 요청
    private String tokenUri;

    //사용자 정보 조회 URL
    private String userInfoUri;




}
