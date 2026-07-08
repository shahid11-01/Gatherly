package com.social.gatherly.Service;


import com.social.gatherly.Entity.Event;
import com.social.gatherly.Entity.EventParticipant;
import com.social.gatherly.Entity.Users;
import com.social.gatherly.Enum.EventStatus;
import com.social.gatherly.Exception.EventNotFoundException;
import com.social.gatherly.Exception.GlobalExceptionHandler;
import com.social.gatherly.Exception.UserNotFoundException;
import com.social.gatherly.Repository.EventParticipantRepository;
import com.social.gatherly.Repository.EventRepository;
import com.social.gatherly.Repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventParticipantService {

    private final EventRepository eventRepository;
    private final UsersRepository usersRepository;
    private final EventParticipantRepository eventParticipantRepository;

    @Transactional
    public void joinRequest(Long userId, Long eventId) {
        Users user = usersRepository.findById(userId).
                orElseThrow(() -> new UserNotFoundException("유저가 없습니다"));

        Event joinedEvent = eventRepository.findById(eventId).
                orElseThrow(() -> new EventNotFoundException("이벤트가 없습니다"));

        //유저가 전에 요청하는지 안 하는지 확인
        boolean alreadyRequested = eventParticipantRepository.
                existsByUserUserIdAndEventEventId(userId, eventId);
        if (alreadyRequested) {
            throw new IllegalArgumentException("이미 요청한 유저입니다");
        }
        EventParticipant joinedParticipant = new EventParticipant();

        joinedParticipant.setParticipantId(userId);
        joinedParticipant.setEvent(joinedEvent);
        joinedParticipant.setUser(user);
        joinedParticipant.setRequestedAt(LocalDateTime.now());
        joinedParticipant.setStatus(EventStatus.PENDING);
        eventParticipantRepository.save(joinedParticipant);


    }


    @Transactional
    public void approveRequest(Long hostId, Long eventId, Long participantUserId) {

        Event event = eventRepository.findById(eventId).orElseThrow(()
                -> new EventNotFoundException("이벤트가 없습니다"));
        //호스트 체크
        if (!event.getHost().getUserId().equals(hostId)) {
            throw new UserNotFoundException("호스트가 아닙니다");
        }

        //승인된 참가자 수 체크
        int approvedCount = eventParticipantRepository
                .countByEventEventIdAndStatus(eventId, EventStatus.APPROVED);
        if (approvedCount >= event.getMaxParticipants()) {
            throw new RuntimeException("최대 참가자 수를 초과했습니다");
        }
        //participant 조회 후 status 변경
        EventParticipant participant = eventParticipantRepository
                .findByUserUserIdAndEventEventId(participantUserId, eventId)
                .orElseThrow(() -> new IllegalArgumentException("참가 요청이 없습니다"));
        participant.setStatus(EventStatus.APPROVED);
        participant.setApprovedAt(LocalDateTime.now());

    }
 


}

