package com.social.gatherly.service;

import com.social.gatherly.configuration.JwtTokenProvider;
import com.social.gatherly.dto.RefreshTokenRequest;
import com.social.gatherly.exception.InvalidRefreshTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final StringRedisTemplate redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;
    private static final String REFRESH_PREFIX = "refresh:";

    /**
     * Refresh Token을 Redis에 저장
     */
    public void saveFreshToken(String email, String refreshToken) {
        //String Key - String value
        redisTemplate.opsForValue().set(
                //key
                REFRESH_PREFIX + email,
                //value
                refreshToken,
                //TTL
                Duration.ofMillis(jwtTokenProvider.getRefreshExpiration())
        );
    }



     //Redis에서 Refresh Token을 조회

    public String getRefreshToken(String email){
        return redisTemplate
                .opsForValue()
                .get(REFRESH_PREFIX + email);
    }

    //Redis에서 Refresh Token 삭제
    public void deleteRefreshToken(String email) {
        redisTemplate.delete(REFRESH_PREFIX + email);
    }

    public String refresh(RefreshTokenRequest refreshTokenRequest) {
        //Refresh Token에서 이메일 추출
        String email = jwtTokenProvider.getEmailFromToken(refreshTokenRequest.getRefreshToken());
        //Redis에 저장된 Refresh Token 조회
        String savedRefreshToken = getRefreshToken(email);

        //Redis에 Refresh Token 없는 경우

        if(savedRefreshToken == null) {
            throw new InvalidRefreshTokenException("Refresh Token이 존제하지 않습니다");
        }
        //Refresh Token 비교
        if(!savedRefreshToken.equals(refreshTokenRequest.getRefreshToken())) {
            throw new InvalidRefreshTokenException("Refresh Token이 일치하지 않습니다");
        }
        //새로운 Access Token 발급
        return jwtTokenProvider.generateAccessToken(email);

    }

    public void logout(String refreshToken) {
        String email = jwtTokenProvider.getEmailFromToken(refreshToken);
        deleteRefreshToken(email);
    }





}
