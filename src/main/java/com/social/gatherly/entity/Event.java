package com.social.gatherly.entity;


import com.social.gatherly.Enum.EventCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
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

    @Column(name="max_participants", nullable = false)
    private int maxParticipants = 1;

    @Enumerated(EnumType.STRING)
    @Column( nullable = false)
    private EventCategory category;

    @Column(name= "created_at", columnDefinition = "DATETIME", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    //이벤트 이미지 리스트
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventImageEntity>imageList = new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    private Users host;

    @OneToMany(mappedBy = "event")
    private List<EventParticipant> participants = new ArrayList<>();


}
