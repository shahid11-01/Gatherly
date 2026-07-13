package com.social.gatherly.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


/**
 * 외부 API(Kakao)와
 * 통신하기 위해 사용합니다
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        //HTTP 요청을 보내는 객체입니다
        return new RestTemplate();
    }
}
