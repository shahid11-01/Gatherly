package com.social.gatherly.Service;


import com.social.gatherly.Configuration.JwtTokenProvider;
import com.social.gatherly.Dto.AuthResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    /**
     *
     * Access Token관 RefreshToken 생성
     * Redis에 Refresh Token 저장
     */
    public AuthResponseDto generateTokens(String email,String message) {

        String accessToken = jwtTokenProvider.generateAccessToken(email);

        String refreshToken = jwtTokenProvider.generateRefreshToken(email);

        refreshTokenService.saveFreshToken(email, refreshToken);

        return new AuthResponseDto(
                accessToken,
                refreshToken,
                message
        );

    }

}
