package com.social.gatherly.Repository;

import com.social.gatherly.Entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long > {

}
