package com.social.gatherly.Repository;

import com.social.gatherly.Entity.EventParticipant;
import com.social.gatherly.Enum.ParticipantStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventParticipantRepository extends JpaRepository<EventParticipant, Long> {
    //joinRequest -중복 요청 체크
    boolean existsByUserUserIdAndEventEventId(Long userId, Long eventId);

    //approveRequest 최대 참가자 수 체크
    Optional<EventParticipant> findByUserUserIdAndEventEventId(Long userId, Long eventId);

    //approveRequest -승인할 participant 조회
    int countByEventEventIdAndStatus(Long eventId, ParticipantStatus participantStatus);
}
