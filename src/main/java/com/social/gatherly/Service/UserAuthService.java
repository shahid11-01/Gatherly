package com.social.gatherly.Service;


import com.social.gatherly.Configuration.JwtTokenProvider;
import com.social.gatherly.Entity.Users;
import com.social.gatherly.Exception.InvalidRefreshTokenException;
import com.social.gatherly.Exception.UserNotFoundException;
import com.social.gatherly.Repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UsersRepository usersRepository;


    private String extractToken(String authHeader) {
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7).trim();
        }
        throw new InvalidRefreshTokenException("토큰이 없습니다");
    }

    public Users getAuthenticatedUser(String autHeader) {
        String token = extractToken(autHeader);
        String userEmail = jwtTokenProvider.getEmailFromToken(token);

        return usersRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("인증된 유저:" + userEmail + ")를 찾을 수 없습니다"));
    }



}
