package com.social.gatherly.Controller;


import com.social.gatherly.Dto.AuthResponseDto;
import com.social.gatherly.Dto.LoginRequestDto;
import com.social.gatherly.Dto.SignUpRequestDto;
import com.social.gatherly.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> register(@RequestBody SignUpRequestDto register) {
        userService.signUp(register);
        return ResponseEntity.ok().body("성공했어요");
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        String token = userService.loginUser(loginRequestDto);
        return ResponseEntity.ok(new AuthResponseDto(token,  "로그인 성공했어요"));
    }

}
