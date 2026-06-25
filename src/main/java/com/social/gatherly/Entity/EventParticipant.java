package com.social.gatherly.Entity;

import com.social.gatherly.Enum.EventStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name =  "event_participant")
public class EventParticipant {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long participantId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status;

    @Column(name="requested_at", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime requestedAt;

    @Column(name = "approved_at", nullable = true, columnDefinition = "DATETIME")
    private LocalDateTime approvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;




}
