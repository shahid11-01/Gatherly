package com.social.gatherly.dto;


import com.social.gatherly.Enum.EventCategory;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventRequestDto {
    private String title;

    private String description;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private int maxParticipants;

    private EventCategory category;


}
