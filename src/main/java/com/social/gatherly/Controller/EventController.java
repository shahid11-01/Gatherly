package com.social.gatherly.Controller;


import com.social.gatherly.Dto.EventImageResponse;
import com.social.gatherly.Dto.EventRequestDto;
import com.social.gatherly.Entity.Event;
import com.social.gatherly.Entity.EventImageEntity;
import com.social.gatherly.Entity.Users;
import com.social.gatherly.Enum.ImageType;
import com.social.gatherly.Service.EventService;
import com.social.gatherly.Service.ImageService;
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
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final ImageService imageService;

    @PostMapping("/events")
    public ResponseEntity<Long> createEvent(@RequestBody EventRequestDto eventRequestDto,
                                            @AuthenticationPrincipal Users user) {
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
                                            @AuthenticationPrincipal Users user,
                                            @RequestParam  Long eventId) {
        eventService.updateEvent(eventRequestDto, user.getUserId(), eventId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{eventId}/events")
    public ResponseEntity<Void> deleteEvent(@AuthenticationPrincipal Users user,
                                            @PathVariable Long eventId) {
        eventService.deleteEvent(eventId, user.getUserId());
        return ResponseEntity.ok().build();
    }

    





}
