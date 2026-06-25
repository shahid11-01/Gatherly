package com.social.gatherly.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "event_image")
@AllArgsConstructor
@NoArgsConstructor
public class EventImageEntity {
    @Id
    @Column(name ="event_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventImageId;

    //무한 순환 참조 방지
    @ToString.Exclude
    @JoinColumn(name = "event_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;

    @Column(name = "event_image_url", columnDefinition = "CHAR(255)", nullable = false)
    private String eventImageUrl;

}
