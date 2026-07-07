package com.social.gatherly.Controller;


import com.social.gatherly.Dto.EventAllResponse;
import com.social.gatherly.Dto.EventImageResponse;
import com.social.gatherly.Dto.EventRequestDto;
import com.social.gatherly.Dto.EventResponseDto;
import com.social.gatherly.Entity.Event;
import com.social.gatherly.Entity.EventImageEntity;
import com.social.gatherly.Entity.Users;
import com.social.gatherly.Enum.ImageType;
import com.social.gatherly.Service.EventService;
import com.social.gatherly.Service.ImageService;
import com.social.gatherly.Service.UserAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final ImageService imageService;
    private final UserAuthService userAuthService;

    @PostMapping("/create")
    public ResponseEntity<Long> createEvent(@RequestBody EventRequestDto eventRequestDto,
                                            @RequestHeader("Authorization") String authHeader) {
        //JWT 에서 email 추줄 -> DB 에서 Users 조회 현재 구조에 맞음
        Users user =  userAuthService.getAuthenticatedUser(authHeader);
        Long eventId = eventService.createEvent(eventRequestDto, user.getUserId());
        return ResponseEntity.ok(eventId);
    }


    @PostMapping("/{eventId}/images")
    public ResponseEntity<List<EventImageResponse>> uploadEventImages(
            @PathVariable Long eventId,
            @RequestParam("images") List<MultipartFile> images
    ) throws IOException {
        List<EventImageResponse> response = eventService.eventImageUpload(eventId, images);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/updateEvents")
    public ResponseEntity<Void> updateEvent(@RequestBody EventRequestDto eventRequestDto,
                                            @RequestHeader("Authorization") String authHeader,
                                            @RequestParam  Long eventId) {
        //JWT 에서 email 추줄 -> DB 에서 Users 조회 현재 구조에 맞음
        Users user = userAuthService.getAuthenticatedUser(authHeader);
        eventService.updateEvent(eventRequestDto, user.getUserId(), eventId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{eventId}/events")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId,
                                            @RequestHeader("Authorization") String authHeader){
        //JWT 에서 email 추줄 -> DB 에서 Users 조회 현재 구조에 맞음
        Users user = userAuthService.getAuthenticatedUser(authHeader);
        eventService.deleteEvent(eventId, user.getUserId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/eventAll{page}")
    public EventAllResponse<EventResponseDto> getAllEvent(
            @PathVariable Integer page
    ) {

        return eventService.getEvents(page);

    }

    





}
