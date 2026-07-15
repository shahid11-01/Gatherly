package com.social.gatherly.service;


import com.social.gatherly.configuration.JwtTokenProvider;
import com.social.gatherly.dto.*;
import com.social.gatherly.entity.Users;
import com.social.gatherly.Enum.Provider;
import com.social.gatherly.Enum.Role;
import com.social.gatherly.exception.DuplicateEmailException;
import com.social.gatherly.exception.UserNotFoundException;
import com.social.gatherly.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserAuthService userAuthService;
    private final RefreshTokenService refreshTokenService;
    private final TokenService tokenService;

    public void signUp(SignUpRequestDto signUpRequestDto) {
        if( usersRepository.findByEmail(signUpRequestDto.getEmail()).isPresent()) {
            throw new DuplicateEmailException("이미 존재한 이메일입니다");
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

    @Transactional
    public void updateUser(UpdateUserRequest updateUserRequest, String email) {
        Users user = usersRepository.findByEmail(email).orElseThrow(()
                -> new UserNotFoundException("유저가 없습니다"));

        if(!user.getProvider().equals(Provider.LOCAL)) {
            throw new RuntimeException("로컬 계정만 요청 가능합니다");
        }
        //이메일 중복 체크
        if(!user.getEmail().equals(updateUserRequest.getEmail())
            && usersRepository.existsByEmail(updateUserRequest.getEmail())) {
            throw new DuplicateEmailException("이미 사용중인 이메일입니다");

        }
        user.setUserName(updateUserRequest.getUserName());
        user.setEmail(updateUserRequest.getEmail());
        usersRepository.save(user);
    }

    //비밀번호를 체크하고 + 바꾸기
    @Transactional
    public void changePassword(ChangePasswordRequest request, String email) {
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("유저가 없습니다"));

        if(!user.getProvider().equals(Provider.LOCAL)) {
            throw new RuntimeException("로컬 계정만 요청 가능합니다");
        }

        if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("현재 비밀번호가 일치하지 않습니다");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        usersRepository.save(user);
    }

    // 비밀번호가 맞는지 체크
    public String confirmPassword(UserPasswordVerifyRequest request, String email ) {
        if(request == null) {
            throw new RuntimeException("비밀번호가 누락되었습니다");
        }

        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("유저가 없습니다"));

        if(!user.getProvider().equals(Provider.LOCAL)) {
            throw new RuntimeException("로컬 계정만 요청 가능합니다");
        }

        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return "인증에 성공하였습니다";
        } else{
            throw new RuntimeException("비밀번호가 일치하지 않습니다");

        }

}


    public AuthResponseDto loginUser(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);


        return tokenService.generateTokens(
                authentication.getName(),
                "Login successful"
        );
    }
}
