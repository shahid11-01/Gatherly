package com.social.gatherly.controller;


import com.social.gatherly.dto.AuthResponseDto;
import com.social.gatherly.dto.google.GoogleLoginRequest;
import com.social.gatherly.service.GoogleAuthService;
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
                googleAuthService.login(request.getIdToken());
                return ResponseEntity.ok(authResponseDto);
    }
}
