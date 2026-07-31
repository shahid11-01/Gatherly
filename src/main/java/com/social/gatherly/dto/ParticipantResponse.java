package com.social.gatherly.dto;

import com.social.gatherly.Enum.ParticipantStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ParticipantResponse {
    private Long participantId;
    private Long userId;
    private String userName;
    private ParticipantStatus status;
    private LocalDateTime requestedAt;

}
