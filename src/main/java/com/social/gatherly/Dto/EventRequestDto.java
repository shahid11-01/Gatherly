package com.social.gatherly.Dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class EventRequestDto {
    private String title;

    private String description;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private int maxParticipants;


}
