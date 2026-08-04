package com.social.gatherly.controller;


import com.social.gatherly.Enum.EventCategory;
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
            @RequestParam("images") List<MultipartFile> images,
            Authentication authentication
    ) throws IOException {
        System.out.println("Images" + images);
        String email = authentication.getName();
        List<EventImageResponse> response = eventService.eventImageUpload(eventId, images,email);
        System.out.println("Response" + response);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/updateEvent/{eventId}")
    public ResponseEntity<String> updateEvent(@RequestBody EventRequestDto eventRequestDto,
                                            Authentication authentication,
                                            @PathVariable  Long eventId) {
        String email = authentication.getName();
        eventService.updateEvent(eventRequestDto, email, eventId);
        System.out.println("Updated Event" + eventId);
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
            @PathVariable Integer page,
            Authentication authentication,
            @RequestParam(required = false)EventCategory category
    ) {

        return eventService.getEvents(page, category);
    }

    @GetMapping("/featured/{page}")
    public EventAllResponse<EventResponseDto> featured (@PathVariable int page) {
        System.out.println("FeaturedEvents" +  page);
        return eventService.getFeatured(page);
    }

    @GetMapping("/nearby/{page}")
    public EventAllResponse<EventResponseDto> nearby (@PathVariable int page) {
        System.out.println("NearbyEvents" +  page);
        return eventService.getNearby(page);
    }

    @GetMapping("/hosted/{page}")
    public EventAllResponse<EventResponseDto> hosted (@PathVariable int page, Authentication authentication) {
        System.out.println("HostedEvents" +  page);
        return eventService.getHosted(authentication.getName(), page);
    }

    @GetMapping("/joined/{page}")
    public EventAllResponse<EventResponseDto> joined(@PathVariable int page, Authentication authentication) {
        System.out.println("Joined Events" +  page);
        return eventService.getJoined(authentication.getName(),page);
    }

    @GetMapping("/pending/{page}")
    public EventAllResponse<EventResponseDto> pending(@PathVariable int page, Authentication authentication) {
        System.out.println("Pending Events" +  page);
        return eventService.getPending(authentication.getName(), page);
    }

    @GetMapping("/{eventId}")
    public  EventResponseDto getEvent(@PathVariable Long eventId, Authentication authentication ) {
        System.out.println("EventId" + eventId );
        return  eventService.getEvent(eventId, authentication.getName());
    }

    @GetMapping("/search/{page}")
    public EventAllResponse<EventResponseDto> search(@PathVariable int page,
                                                     @RequestParam String title) {
        return eventService.searchEvent(title, page);
    }

    





}
