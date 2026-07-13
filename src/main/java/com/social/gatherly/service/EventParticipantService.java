package com.social.gatherly.service;


import com.social.gatherly.entity.Event;
import com.social.gatherly.entity.EventParticipant;
import com.social.gatherly.entity.Users;
import com.social.gatherly.Enum.ParticipantStatus;
import com.social.gatherly.exception.EventNotFoundException;
import com.social.gatherly.exception.UserNotFoundException;
import com.social.gatherly.repository.EventParticipantRepository;
import com.social.gatherly.repository.EventRepository;
import com.social.gatherly.repository.UsersRepository;
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

        Event joinedEvent = getEvent(eventId);

        //유저가 전에 요청하는지 안 하는지 확인
        boolean alreadyRequested = eventParticipantRepository.
                existsByUserUserIdAndEventEventId(userId, eventId);
        if (alreadyRequested) {
            throw new IllegalArgumentException("이미 요청한 유저입니다");
        }
        EventParticipant joinedParticipant = new EventParticipant();

        joinedParticipant.setParticipantId(joinedParticipant.getParticipantId());
        joinedParticipant.setEvent(joinedEvent);
        joinedParticipant.setRequestedAt(LocalDateTime.now());
        joinedParticipant.setStatus(ParticipantStatus.PENDING);
        joinedParticipant.setUser(user);
        eventParticipantRepository.save(joinedParticipant);

    }


    @Transactional
    public void approveRequest(Long hostId, Long eventId, Long participantUserId) {

        Event event = getEvent(eventId);
        //호스트 체크
        if (!event.getHost().getUserId().equals(hostId)) {
            throw new UserNotFoundException("호스트가 아닙니다");
        }

        //승인된 참가자 수 체크

        int approvedCount = eventParticipantRepository
                .countByEventEventIdAndStatus(eventId, ParticipantStatus.APPROVED);
        if (approvedCount >= event.getMaxParticipants()) {
            throw new RuntimeException("최대 참가자 수를 초과했습니다");
        }
        //participant 조회 후 status 변경
        EventParticipant participant = eventParticipantRepository
                .findByUserUserIdAndEventEventId(participantUserId, eventId)
                .orElseThrow(() -> new IllegalArgumentException("참가 요청이 없습니다"));
        if(participant.getStatus() != ParticipantStatus.PENDING) {
            throw  new IllegalArgumentException("대기 중인 신청만 수락 할 수 있습니다");
        }
        participant.setStatus(ParticipantStatus.APPROVED);
        participant.setApprovedAt(LocalDateTime.now());

    }

    @Transactional
    public void rejectRequest(Long hostId, Long eventId, Long participantUserId) {
        Event event = getEvent(eventId);
        //호스트 체크
        if (!event.getHost().getUserId().equals(hostId)) {
            throw new UserNotFoundException("호스트가 아닙니다");
        }

        EventParticipant participant = eventParticipantRepository.
                findByUserUserIdAndEventEventId(participantUserId,eventId)
                .orElseThrow(() -> new IllegalArgumentException("참가 요청이 없습니다"));

        if(participant.getStatus() != ParticipantStatus.PENDING) {
            throw new IllegalArgumentException("대기 중인 신청만 거절할 수 있습니다");
        }
        participant.setStatus(ParticipantStatus.REJECTED);
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("이벤트가 없습니다"));
    }

    private EventParticipant getApprovedParticipant(Long eventId, Long userId) {
        EventParticipant participant = eventParticipantRepository
                .findByUserUserIdAndEventEventId(userId, eventId)
                .orElseThrow(() -> new IllegalArgumentException("참가자가 없습니다"));
        if(participant.getStatus() != ParticipantStatus.APPROVED) {
            throw new IllegalArgumentException("승인된 참가자만 가능합니다");
        }
        return participant;
    }
    private void removeParticipant(Long eventId, Long userId) {
        EventParticipant participant = getApprovedParticipant(eventId, userId);
        eventParticipantRepository.delete(participant);
    }

    @Transactional
    public void cancelRequest(Long eventId, Long requesterUserId) {
        EventParticipant participant = eventParticipantRepository.
                findByUserUserIdAndEventEventId(requesterUserId,eventId)
                .orElseThrow(() -> new IllegalArgumentException("참가 요청이 없습니다"));

       if(participant.getStatus() != ParticipantStatus.PENDING) {
           throw new IllegalArgumentException("대기 중인 신청만 취소할 수 있습니다");
       }
       eventParticipantRepository.delete(participant);
    }


    public void deleteParticipant(Long eventId, Long userId) {
        removeParticipant(eventId,userId);
    }

    public void leaveEvent(Long eventId, Long loginUserId) {
        removeParticipant(eventId,loginUserId);
    }



}

