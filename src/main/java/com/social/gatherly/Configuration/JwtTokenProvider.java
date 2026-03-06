package com.social.gatherly.Configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtTokenProvider {
    @Value("{jwt.secret}")
    private String secretKey;

    @Value("{jwt.expiration}")
    private long expirationTime;

    public String generateToken(String subject) {
        Date now = new Date(); //현재 시간
        Date expiryDate = new Date(now.getTime() + expirationTime); //만료 시간

        //jwt 생성
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSignInKey())
                .compact();
    }
    //JWT에서 email 추줄
    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }


    //토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().
                    setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private <T> T getClaimFromToken(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // 전체 Claims 가져오기
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getClaimFromToken(token, Claims::getExpiration);
        return expiration.before(new Date());
    }


}
