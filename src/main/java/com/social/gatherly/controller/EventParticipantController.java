package com.social.gatherly.controller;


import com.social.gatherly.dto.ParticipantResponse;
import com.social.gatherly.entity.Users;
import com.social.gatherly.service.EventParticipantService;
import com.social.gatherly.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/participant")
@RequiredArgsConstructor
public class EventParticipantController {
    private final EventParticipantService eventParticipantService;
    private final UserAuthService userAuthService;

    @PostMapping("/join/{eventId}")
    public ResponseEntity<String> joinRequest(@PathVariable Long eventId,
                                              Authentication authentication) {

        String email = authentication.getName();
        eventParticipantService.joinRequest(email,eventId);
        return ResponseEntity.ok("참가 요청 완료");

    }

    @PatchMapping("/approve/{participantUserId}/{eventId}")
    public ResponseEntity<String> approveRequest(@PathVariable Long eventId,
                                               @PathVariable  Long participantUserId,
                                               Authentication authentication) {
        String email = authentication.getName();
        eventParticipantService.approveRequest(email,eventId,participantUserId);
        return ResponseEntity.ok("참가 요청 승인 완료");

    }

    @PatchMapping("/reject/{participantUserId}/{eventId}")
    public ResponseEntity<String> rejectRequest(@PathVariable Long eventId,
                                              @PathVariable Long participantUserId,
                                              Authentication authentication) {
        String email = authentication.getName();
        eventParticipantService.rejectRequest(email, eventId, participantUserId);

        return ResponseEntity.ok("참가 요청 거절 완료");
    }

    @GetMapping("/participants/{eventId}")
    public ResponseEntity<List<ParticipantResponse>> getParticipants(
            @PathVariable Long eventId,
            Authentication authentication){
        return  ResponseEntity.ok(
                eventParticipantService.getParticipants(authentication.getName(), eventId));
    }


    @DeleteMapping("/cancel/{eventId}")
    public ResponseEntity<String> cancelRequest(@PathVariable Long eventId,
                                              Authentication authentication) {
        String email = authentication.getName();
        eventParticipantService.cancelRequest(eventId, email);
        return ResponseEntity.ok("참가 취소 완료");
    }

    @DeleteMapping("/deleteParticipant/{participantUserId}/{eventId}")
    public ResponseEntity<String> deleteParticipant(@PathVariable Long eventId,
                                                  @PathVariable Long participantUserId,
                                                  Authentication authentication) {
        String email = authentication.getName();
        eventParticipantService.deleteParticipant(eventId,participantUserId, email);
        return ResponseEntity.ok("참가자 삭제 완료");
    }

    @DeleteMapping("/leaveEvent/{eventId}")
    public ResponseEntity<String> leaveEvent(@PathVariable Long eventId,
                                           Authentication authentication) {
        String email = authentication.getName();
        eventParticipantService.leaveEvent(eventId, email);
        return ResponseEntity.ok("이벤트 탈퇴 완료");
    }




}
