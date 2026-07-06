package com.social.gatherly.Controller;


import com.social.gatherly.Configuration.JwtTokenProvider;
import com.social.gatherly.Dto.*;
import com.social.gatherly.Entity.Users;
import com.social.gatherly.Service.RefreshTokenService;
import com.social.gatherly.Service.TokenService;
import com.social.gatherly.Service.UserAuthService;
import com.social.gatherly.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
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
                                             @RequestHeader("Authorization") String authHeader
    ) {
        Users user = userAuthService.getAuthenticatedUser(authHeader);
        userService.updateUser(updateUserRequest, user.getUserId());
        return ResponseEntity.ok().body("유저 정보 수정이 성공했습니다");
    }

    @PatchMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest,
                                                 @RequestHeader("Authorization") String authHeader

    ) {
        Users user = userAuthService.getAuthenticatedUser(authHeader);
        userService.changePassword(changePasswordRequest, user.getUserId());
        return ResponseEntity.ok().body("비밀번호가 변경되었습니다");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyUserByPassword(
            @RequestBody UserPasswordVerifyRequest userPasswordVerifyRequest,
            @RequestHeader("Authorization") String authHeader
    ) {
        Users user = userAuthService.getAuthenticatedUser(authHeader);
        String result = userService.confirmPassword(userPasswordVerifyRequest.getPassword(), user.getUserId());
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
         String newAccessToken = refreshTokenService.refresh(refreshTokenRequest);
         return ResponseEntity.ok(newAccessToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody RefreshTokenRequest request) {
            refreshTokenService.logout(request.getRefreshToken());
            return ResponseEntity.ok().build();
    }




}
