package com.social.gatherly.Controller;


import com.social.gatherly.Dto.AuthResponseDto;
import com.social.gatherly.Dto.Google.GoogleLoginRequest;
import com.social.gatherly.Service.GoogleAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/google")
@RequiredArgsConstructor
public class GoogleController {
    private final GoogleAuthService googleAuthService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @RequestBody GoogleLoginRequest request) {
        AuthResponseDto authResponseDto =
                googleAuthService.login(request.getCode());
                return ResponseEntity.ok(authResponseDto);
    }
}
