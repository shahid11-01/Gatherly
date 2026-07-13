package com.social.gatherly.controller;

import com.social.gatherly.dto.AuthResponseDto;
import com.social.gatherly.dto.kakao.KakaoLoginRequest;
import com.social.gatherly.service.KakaoAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kakao")
@RequiredArgsConstructor
public class KakaoController {
    private final KakaoAuthService kakaoAuthService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @RequestBody KakaoLoginRequest request
    ) {
        AuthResponseDto responseDto = kakaoAuthService.login(request.getCode());
        return ResponseEntity.ok(responseDto);
    }
}
