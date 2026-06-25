package com.social.gatherly.Controller;


import com.social.gatherly.Entity.Users;
import com.social.gatherly.Service.CustomUserDetailsService;
import com.social.gatherly.Service.EventParticipantService;
import com.social.gatherly.Service.UserAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class EventParticipantController {
    private final EventParticipantService eventParticipantService;
    private final UserAuthService userAuthService;

    @PostMapping("/{eventId}/join")
    public ResponseEntity<Void> joinRequest(@PathVariable Long eventId,
                                            @RequestHeader("Authorization") String authHeader) {
        //JWT 에서 email 추줄 -> DB 에서 Users 조회 현재 구조에 맞음
        Users user = userAuthService.getAuthenticatedUser(authHeader);
        eventParticipantService.joinRequest(user.getUserId(),eventId);
        return ResponseEntity.noContent().build();

    }

    @PatchMapping("/{eventId}/approve/{participantUserId}")
    public ResponseEntity<Void> approveRequest(@PathVariable Long eventId,
                                               @PathVariable  Long participantUserId,
                                               @RequestHeader("Authorization") String authHeader) {
        //JWT 에서 email 추줄 -> DB 에서 Users 조회 현재 구조에 맞음
        Users user = userAuthService.getAuthenticatedUser(authHeader);
        eventParticipantService.approveRequest(user.getUserId(),eventId,participantUserId);
        return ResponseEntity.noContent().build();
    }
}
