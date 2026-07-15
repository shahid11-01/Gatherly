package com.social.gatherly.repository;

import com.social.gatherly.entity.Event;
import com.social.gatherly.entity.EventParticipant;
import com.social.gatherly.Enum.ParticipantStatus;
import com.social.gatherly.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventParticipantRepository extends JpaRepository<EventParticipant, Long> {
    //joinRequest -중복 요청 체크
    boolean existsByUserUserIdAndEventEventId(Long userId, Long eventId);

    boolean existsByUserAndEvent(Users user, Event event);

    //approveRequest 최대 참가자 수 체크
    Optional<EventParticipant> findByUserUserIdAndEventEventId(Long userId, Long eventId);
    Optional<EventParticipant> findByUserEmailAndEventEventId(String email, Long eventId);

    Optional<EventParticipant> findByParticipantIdAndEventEventId(Long participantId,Long eventId);

    //approveRequest -승인할 participant 조회
    int countByEventEventIdAndStatus(Long eventId, ParticipantStatus participantStatus);
}
