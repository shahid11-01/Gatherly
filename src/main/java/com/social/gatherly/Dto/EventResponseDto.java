package com.social.gatherly.Dto;


import com.social.gatherly.Entity.Event;
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
    private int maxParticipants;

   public static EventResponseDto from(Event event) {

       return EventResponseDto.builder()
               .eventId(event.getEventId())
               .title(event.getTitle())
               .description(event.getDescription())
               .startDate(event.getStartDate())
               .endDate(event.getEndDate())
               .maxParticipants(event.getMaxParticipants())
               .build();

   }
}