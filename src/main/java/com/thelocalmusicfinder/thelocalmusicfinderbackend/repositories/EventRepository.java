package com.thelocalmusicfinder.thelocalmusicfinderbackend.repositories;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Event;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Venue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
  Optional<Event> findByEventCode(String eventCode);

  List<Event> findByVenueAndEventDate(Venue venue, LocalDate date);

  @Query("""
  SELECT e FROM Event e
  WHERE e.eventDate > :currentDate
     OR (e.eventDate = :currentDate AND e.startTime > :currentTime)
  """)
  List<Event> findEventsAfter(
          @Param("currentDate") LocalDate currentDate,
          @Param("currentTime") LocalTime currentTime
  );

  void deleteByEventCode(String eventCode);
}
