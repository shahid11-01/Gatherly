package com.social.gatherly.repository;

import com.social.gatherly.Enum.EventCategory;
import com.social.gatherly.entity.Event;
import com.social.gatherly.Enum.ParticipantStatus;
import com.social.gatherly.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface EventRepository extends JpaRepository<Event, Long > {
    //upcoming 이벤트
    Page<Event> findByStartDateAfterOrderByStartDateAsc(LocalDateTime now, Pageable pageable);
    Page<Event> findByCategory(EventCategory category, Pageable pageable);
    //인기 있는 이벤트
    @Query(
            value = "SELECT e FROM Event e LEFT JOIN e.participants p GROUP BY e ORDER BY COUNT(p) DESC",
            countQuery = "SELECT COUNT(e) FROM Event e"
    )
    Page<Event> findAllOrderByParticipantCountDesc(Pageable pageable);

    //hosted 이벤트
    Page<Event> findByHostOrderByStartDateDesc(Users host, Pageable pageable);
    long countByHost(Users host);

    //참여한 이벤트
    @Query(
            value = "SELECT p.event FROM EventParticipant p WHERE p.user.email = :email AND p.status = :status",
            countQuery = "SELECT COUNT(p) FROM EventParticipant p WHERE p.user.email = :email AND p.status = :status"
    )
    Page<Event> findJoinedByEmail(@Param("email") String email,
                                  @Param("status") ParticipantStatus status,
                                  Pageable pageable);

}
