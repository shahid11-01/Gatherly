package com.social.gatherly.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {
    /**
     * RedisConnectionFactory 생성
     * Redis 서버에 연결
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        //Redis에서 문자열(String) 데이터를 쉽게 저장하고 조회하기 위하 클래스이다.
        return new StringRedisTemplate(connectionFactory);
    }
}
