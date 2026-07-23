package com.social.gatherly.controller;


import com.social.gatherly.configuration.JwtTokenProvider;
import com.social.gatherly.dto.*;
import com.social.gatherly.entity.Users;
import com.social.gatherly.service.RefreshTokenService;
import com.social.gatherly.service.TokenService;
import com.social.gatherly.service.UserAuthService;
import com.social.gatherly.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    private final UserAuthService userAuthService;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<String> register(@RequestBody SignUpRequestDto register) {
        userService.signUp(register);
        return ResponseEntity.ok().body("성공했어요");
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        AuthResponseDto authResponseDto = userService.loginUser(loginRequestDto);
        return ResponseEntity.ok().body(authResponseDto);
    }


    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody UpdateUserRequest updateUserRequest,
                                             Authentication  authentication
    ) {
        String email = authentication.getName();
        userService.updateUser(updateUserRequest, email);
        return ResponseEntity.ok("유저 정보 수정이 성공했습니다");

    }

    @PatchMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest,
                                                Authentication authentication

    ) {
       String email = authentication.getName();
       userService.changePassword(changePasswordRequest, email);
        return ResponseEntity.ok().body("비밀번호가 변경되었습니다");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyUserByPassword(
            @RequestBody UserPasswordVerifyRequest request,
           Authentication authentication
    ) {
        String email = authentication.getName();
        userService.confirmPassword(request, email);
        return ResponseEntity.ok().body("비밀번호 인증이 되었습니다");
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
         String newAccessToken = refreshTokenService.refresh(refreshTokenRequest);
         return ResponseEntity.ok(newAccessToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody RefreshTokenRequest request) {
            refreshTokenService.logout(request.getRefreshToken());
            return ResponseEntity.ok("로그아웃 되었습니다");
    }

    @GetMapping("/user/me")
    public ResponseEntity<UserResponseDto> getCurrenUser(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(userService.getCurrentUser(email));
    }




}
