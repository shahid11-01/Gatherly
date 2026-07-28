package com.social.gatherly.dto;


import com.social.gatherly.Enum.EventCategory;
import com.social.gatherly.entity.Event;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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

   public static EventResponseDto from(Event event) {

       return EventResponseDto.builder()
               .eventId(event.getEventId())
               .title(event.getTitle())
               .description(event.getDescription())
               .startDate(event.getStartDate())
               .endDate(event.getEndDate())
               .category(event.getCategory())
               .maxParticipants(event.getMaxParticipants())
               .build();

   }
}