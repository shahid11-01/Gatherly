package com.social.gatherly.Service;


import com.social.gatherly.Configuration.JwtTokenProvider;
import com.social.gatherly.Dto.LoginRequestDto;
import com.social.gatherly.Dto.SignUpRequestDto;
import com.social.gatherly.Entity.Users;
import com.social.gatherly.Enum.Provider;
import com.social.gatherly.Enum.Role;
import com.social.gatherly.Repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public void signUp(SignUpRequestDto signUpRequestDto) {
        if( usersRepository.findByEmail(signUpRequestDto.getEmail()).isPresent()) {
            throw new RuntimeException("이미 존재한 이메일입니다");
        }
        Users user = new Users();
        user.setUserName(signUpRequestDto.getUserName());
        user.setEmail(signUpRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));
        user.setProvider(Provider.LOCAL);
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setModifiedAt(LocalDateTime.now());
        usersRepository.save(user);
    }

    public String loginUser(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken(authentication.getName());
    }
}
