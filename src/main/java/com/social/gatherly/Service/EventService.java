package com.social.gatherly.Service;


import com.social.gatherly.Configuration.GlobalConfig;
import com.social.gatherly.Dto.EventAllResponse;
import com.social.gatherly.Dto.EventImageResponse;
import com.social.gatherly.Dto.EventRequestDto;
import com.social.gatherly.Dto.EventResponseDto;
import com.social.gatherly.Entity.Event;
import com.social.gatherly.Entity.EventImageEntity;
import com.social.gatherly.Entity.Users;
import com.social.gatherly.Enum.ImageType;
import com.social.gatherly.Repository.EventImageRepository;
import com.social.gatherly.Repository.EventRepository;
import com.social.gatherly.Repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UsersRepository usersRepository;
    private final ImageService imageService;
    private final EventImageRepository eventImageRepository;
    private final GlobalConfig globalConfig;


    public Long createEvent(EventRequestDto eventRequestDto, Long userId) {
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
        Event savedEvent = eventRepository.save(events);
        return savedEvent.getEventId();

    }

    public void updateEvent(EventRequestDto eventRequestDto, Long userId, Long eventId) {
        Event updatedEvent = eventRepository.findById(eventId)
                        .orElseThrow(() -> new RuntimeException("이벤트가 없습니다"));

        if(eventRequestDto.getEndDate().isBefore(eventRequestDto.getStartDate())) {
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
        Event event = eventRepository.findById(eventId)
                        .orElseThrow(() -> new RuntimeException("이벤트가 없습니다"));
        if(!event.getHost().getUserId().equals(userId)) {
            throw new RuntimeException("권한 없음");
        }
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

    public List<EventImageResponse> eventImageUpload(Long eventId, List<MultipartFile> images) throws IOException {
        Event events = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("이벤트가 없습니다"));
        if(images.isEmpty()) {
            throw new IOException("이미지가 없습니다");
        }

        //파일 저장 + URL 경로 리스트 받기
        List<String> filePathList = imageService.eventImageUpload(events, images,ImageType.EVENT);

        //DB에 EventImageEntity로 저장
        List<EventImageEntity> entites = filePathList.stream()
                .map(filePath -> new EventImageEntity(null, events, filePath))
                .toList();
        List<EventImageEntity> savedEntities = eventImageRepository.saveAll(entites);

        // 응답 저장
        return savedEntities.stream()
                .map(entity -> EventImageResponse.builder()
                        .id(entity.getEventImageId())
                        .url(globalConfig.getDomain() + entity.getEventImageUrl())
                        .build())
                .toList();
    }


}
