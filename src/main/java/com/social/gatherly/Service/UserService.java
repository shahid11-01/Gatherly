package com.social.gatherly.Service;


import com.social.gatherly.Configuration.JwtTokenProvider;
import com.social.gatherly.Dto.LoginRequestDto;
import com.social.gatherly.Dto.SignUpRequestDto;
import com.social.gatherly.Dto.UpdateUserRequest;
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
public class UserService {
    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserAuthService userAuthService;

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

    public void updateUser(UpdateUserRequest updateUserRequest, Long userId) {
        Users user = usersRepository.findById(userId).orElseThrow(()
                -> new RuntimeException("유저가 없습니다"));
        if(!user.getProvider().equals(Provider.LOCAL)) {
            throw new RuntimeException("라헬론 계정만 요청 가능합니다");
        }
        Users updatedUser = new Users();
        updatedUser.setUserName(updateUserRequest.getUserName());
        updatedUser.setEmail(updateUserRequest.getEmail());
        updatedUser.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
        usersRepository.save(updatedUser);
    }


    public String confirmPassword(String password, Long userId ) {
        if(password == null) {
            throw new RuntimeException("비밀번호가 누락되었습니다");
        }

        Users user = usersRepository.findById(userId).orElse(null);

        if(!user.getProvider().equals(Provider.LOCAL)) {
            throw new RuntimeException("Gatherly 계정만 요청 가능합니다");
        }

        if (passwordEncoder.matches(password, user.getPassword())) {
            return "인증에 성공하였습니다";
        } else{
            throw new RuntimeException("비밀번호가 일치하지 않습니다");

        }

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
