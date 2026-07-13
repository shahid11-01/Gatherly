package com.social.gatherly.service;


import com.social.gatherly.configuration.JwtTokenProvider;
import com.social.gatherly.entity.Users;
import com.social.gatherly.exception.InvalidRefreshTokenException;
import com.social.gatherly.exception.UserNotFoundException;
import com.social.gatherly.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UsersRepository usersRepository;


    private String extractToken(String authHeader) {
        if(authHeader != null && authHeader.startsWith("Bearer")) {
            return authHeader.substring(7).trim();
        }
        throw new InvalidRefreshTokenException("토큰이 없습니다");
    }

    public Users getAuthenticatedUser(String autHeader) {
        System.out.println("Header= " +autHeader);
        String token = extractToken(autHeader);
        System.out.println("Token" + token);
        String userEmail = jwtTokenProvider.getEmailFromToken(token);
        System.out.println("UserEmail" + userEmail);

        return usersRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("인증된 유저:" + userEmail + ")를 찾을 수 없습니다"));
    }



}
