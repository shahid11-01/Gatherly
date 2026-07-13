package com.social.gatherly.controller;


import com.social.gatherly.entity.Users;
import com.social.gatherly.service.EventParticipantService;
import com.social.gatherly.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/participant")
@RequiredArgsConstructor
public class EventParticipantController {
    private final EventParticipantService eventParticipantService;
    private final UserAuthService userAuthService;

    @PostMapping("/{eventId}/join")
    public ResponseEntity<Void> joinRequest(@PathVariable Long eventId,
                                            @RequestHeader("Authorization") String authHeader) {
        //JWT 에서 email 추줄 -> DB 에서 Users 조회 현재 구조에 맞음
        log.info("evenId:" + eventId);
        System.out.println("evenId" + eventId);
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

    @PatchMapping("/{eventId}/reject/{participantUserId}")
    public ResponseEntity<Void> rejectRequest(@PathVariable Long eventId,
                                              @PathVariable Long participantUserId,
                                              @RequestHeader("Authorization") String authHeader) {
        Users user = userAuthService.getAuthenticatedUser(authHeader);
        eventParticipantService.rejectRequest(user.getUserId(), eventId, participantUserId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{eventId}/cancel")
    public ResponseEntity<Void> cancelRequest(@PathVariable Long eventId,
                                              @RequestHeader("Authorization") String authHeader) {
        Users requesterId = userAuthService.getAuthenticatedUser(authHeader);
        eventParticipantService.cancelRequest(requesterId.getUserId(),eventId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{eventId}/deleteParticipant")
    public ResponseEntity<Void> deleteParticipant(@PathVariable Long eventId,
                                                  @RequestHeader("Authorization") String authHeader) {
        Users user = userAuthService.getAuthenticatedUser(authHeader);
        eventParticipantService.deleteParticipant(user.getUserId(),eventId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{eventId}/leaveEvent")
    public ResponseEntity<Void> leaveEvent(@PathVariable Long eventId,
                                           @RequestHeader("Authorization") String authHeader) {
        Users user = userAuthService.getAuthenticatedUser(authHeader);
        eventParticipantService.leaveEvent(user.getUserId(), eventId);
        return ResponseEntity.noContent().build();
    }



}
