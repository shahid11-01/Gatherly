package com.social.gatherly.Repository;


import com.social.gatherly.Entity.EventImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventImageRepository extends JpaRepository<EventImageEntity, Long> {
    @Query("SELECT e.eventImageUrl  FROM EventImageEntity  e WHERE e.event.eventId = :eventId")
    List<String> findEventImageUrlsByEventId(@Param("eventId") Long eventId);
}
