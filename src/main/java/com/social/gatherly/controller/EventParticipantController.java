package com.social.gatherly.controller;


import com.social.gatherly.entity.Users;
import com.social.gatherly.service.EventParticipantService;
import com.social.gatherly.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/participant")
@RequiredArgsConstructor
public class EventParticipantController {
    private final EventParticipantService eventParticipantService;
    private final UserAuthService userAuthService;

    @PostMapping("/{eventId}/join")
    public ResponseEntity<String> joinRequest(@PathVariable Long eventId,
                                              Authentication authentication) {

        String email = authentication.getName();
        eventParticipantService.joinRequest(email,eventId);
        return ResponseEntity.ok("참가 요청 완료");

    }

    @PatchMapping("/{eventId}/approve/{participantUserId}")
    public ResponseEntity<String> approveRequest(@PathVariable Long eventId,
                                               @PathVariable  Long participantUserId,
                                               Authentication authentication) {
        String email = authentication.getName();
        eventParticipantService.approveRequest(email,eventId,participantUserId);
        return ResponseEntity.ok("참가 요청 승인 완료");

    }

    @PatchMapping("/{eventId}/reject/{participantUserId}")
    public ResponseEntity<String> rejectRequest(@PathVariable Long eventId,
                                              @PathVariable Long participantUserId,
                                              Authentication authentication) {
        String email = authentication.getName();
        eventParticipantService.rejectRequest(email, eventId, participantUserId);

        return ResponseEntity.ok("참가 요청 거절 완료");
    }

    @DeleteMapping("/{eventId}/cancel")
    public ResponseEntity<String> cancelRequest(@PathVariable Long eventId,
                                              Authentication authentication) {
        String email = authentication.getName();
        eventParticipantService.cancelRequest(eventId, email);
        return ResponseEntity.ok("참가 취소 완료");
    }

    @DeleteMapping("/{eventId}/deleteParticipant/{participantUserId}")
    public ResponseEntity<String> deleteParticipant(@PathVariable Long eventId,
                                                  @PathVariable Long participantUserId,
                                                  Authentication authentication) {
        String email = authentication.getName();
        eventParticipantService.deleteParticipant(eventId,participantUserId, email);
        return ResponseEntity.ok("참가자 삭제 완료");
    }

    @DeleteMapping("/{eventId}/leaveEvent")
    public ResponseEntity<String> leaveEvent(@PathVariable Long eventId,
                                           Authentication authentication) {
        String email = authentication.getName();
        eventParticipantService.leaveEvent(eventId, email);
        return ResponseEntity.ok("이벤트 탈퇴 완료");
    }



}
