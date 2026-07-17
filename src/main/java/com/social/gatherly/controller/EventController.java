package com.social.gatherly.controller;


import com.social.gatherly.dto.EventAllResponse;
import com.social.gatherly.dto.EventImageResponse;
import com.social.gatherly.dto.EventRequestDto;
import com.social.gatherly.dto.EventResponseDto;
import com.social.gatherly.entity.Users;
import com.social.gatherly.service.EventService;
import com.social.gatherly.service.ImageService;
import com.social.gatherly.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
                                            Authentication authentication) {
        String email = authentication.getName();
        Long eventId = eventService.createEvent(eventRequestDto, email);
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

    @PutMapping("/{eventId}")
    public ResponseEntity<String> updateEvent(@RequestBody EventRequestDto eventRequestDto,
                                            Authentication authentication,
                                            @RequestParam  Long eventId) {
        String email = authentication.getName();
        eventService.updateEvent(eventRequestDto, email, eventId);
        return ResponseEntity.ok("이벤트 정보가 수정되었습니다");
    }

    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long eventId,
                                            Authentication authentication){
        String email = authentication.getName();
        eventService.deleteEvent(eventId, email);
        return ResponseEntity.ok("이벤트가 삭제되었습니다");
    }

    @GetMapping("/eventAll/{page}")
    public EventAllResponse<EventResponseDto> getAllEvent(
            @PathVariable Integer page
    ) {

        return eventService.getEvents(page);

    }



    





}
