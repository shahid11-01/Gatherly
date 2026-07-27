package com.social.gatherly.repository;

import com.social.gatherly.Enum.EventCategory;
import com.social.gatherly.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long > {
    Page<Event> findByCategory(EventCategory category, Pageable pageable);

}
