package com.social.gatherly.repository;

import com.social.gatherly.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long > {

}
