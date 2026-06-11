package com.social.gatherly.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @Column(name = "title", columnDefinition = "CHAR(100)", nullable = false, length = 100)
    private String title;

    @Column(name= "description",columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name="start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name="end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name="max_participants",columnDefinition = "Integer", nullable = false)
    private int maxParticipants = 0;

    @Column(name= "created_at", columnDefinition = "DATETIME", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    private Users host;

    @OneToMany(mappedBy = "event")
    private List<EventParticipant> participants = new ArrayList<>();


}
