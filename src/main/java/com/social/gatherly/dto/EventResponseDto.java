package com.social.gatherly.dto;


import com.social.gatherly.Enum.EventCategory;
import com.social.gatherly.Enum.ParticipantStatus;
import com.social.gatherly.entity.Event;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
public class EventResponseDto {
    private Long eventId;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private EventCategory category;
    private int maxParticipants;
    private List<String> imageUrls;
    private long participantCount;

   public static EventResponseDto from(Event event, String domain) {

       List<String> imageUrls = event.getImageList().stream()
               .map(img -> domain + img.getEventImageUrl())
               .toList();

       long participantCount = event.getParticipants().stream()
               .filter(p -> p.getStatus() == ParticipantStatus.APPROVED)
               .count();

       return EventResponseDto.builder()
               .eventId(event.getEventId())
               .title(event.getTitle())
               .description(event.getDescription())
               .startDate(event.getStartDate())
               .endDate(event.getEndDate())
               .category(event.getCategory())
               .imageUrls(imageUrls)
               .participantCount(participantCount)
               .maxParticipants(event.getMaxParticipants())
               .build();

   }
}