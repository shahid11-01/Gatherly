package com.social.gatherly.Service;


import com.social.gatherly.Dto.EventAllResponse;
import com.social.gatherly.Dto.EventRequestDto;
import com.social.gatherly.Dto.EventResponseDto;
import com.social.gatherly.Entity.Event;
import com.social.gatherly.Entity.Users;
import com.social.gatherly.Repository.EventRepository;
import com.social.gatherly.Repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UsersRepository usersRepository;


    public void createEvent(EventRequestDto eventRequestDto, Long userId) {
        Users host = usersRepository.findById(userId).orElseThrow(()
                -> new RuntimeException("유저가 없습니다"));
        if(eventRequestDto.getEndDate().isBefore(eventRequestDto.getStartDate())) {
            throw new IllegalArgumentException("종료일은 시작일보다 빠를 수 없습니다");
        }
        Event events = new Event();
        events.setTitle(eventRequestDto.getTitle());
        events.setDescription(eventRequestDto.getDescription());
        events.setStartDate(eventRequestDto.getStartDate());
        events.setEndDate(eventRequestDto.getEndDate());
        events.setMaxParticipants(eventRequestDto.getMaxParticipants());
        events.setHost(host);
        eventRepository.save(events);

    }

    public void updateEvent(EventRequestDto eventRequestDto, Long userId, Long eventId) {
        Event updatedEvent = eventRepository.findById(eventId)
                        .orElseThrow(() -> new RuntimeException("이벤트가 없습니다"));

        if(eventRequestDto.getEndDate().isBefore(updatedEvent.getStartDate())) {
            throw new IllegalArgumentException("종료일은 시작일보다 빠를 수 없습니다");
        }


        if(!updatedEvent.getHost().getUserId().equals(userId)) {
            throw new RuntimeException("권한 없음");
        }
        updatedEvent.setTitle(eventRequestDto.getTitle());
        updatedEvent.setDescription(eventRequestDto.getDescription());
        updatedEvent.setStartDate(eventRequestDto.getStartDate());
        updatedEvent.setEndDate(eventRequestDto.getEndDate());
        updatedEvent.setMaxParticipants(eventRequestDto.getMaxParticipants());
        eventRepository.save(updatedEvent);

    }


    public void deleteEvent(Long eventId, Long userId) {
        eventRepository.deleteById(eventId);
    }


    public EventAllResponse<EventResponseDto> getEvents(Integer pageNum) {
        //pageable 생성
        Pageable page = PageRequest.of(pageNum, 10);
        Page<Event> events = eventRepository.findAll(page);

        //DTO 변환-> page 안의 실제 event 목록 꺼내기(하나씩 꺼냄)
        List<EventResponseDto> eventResponseDtoList = events.getContent()
                .stream()
                .map(EventResponseDto::from)
                .toList();

        return EventAllResponse.<EventResponseDto>builder()
                .events(eventResponseDtoList)
                .page(events.getNumber())
                .size(events.getSize())
                .total(events.getTotalElements())
                .totalPages(events.getTotalPages())
                .last(events.isLast())
                .build();
    }


}
