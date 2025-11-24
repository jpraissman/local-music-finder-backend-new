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
  WHERE e.eventDate >= :startDate
  """)
  List<Event> findEventsAfter(@Param("currentDate") LocalDate startDate);

  @Query("""
  SELECT e FROM Event e
  WHERE e.eventDate >= :startDate
    AND e.eventDate <= :endDate
  """)
  List<Event> findEventsBetween(
          @Param("startDate") LocalDate startDate,
          @Param("endDate") LocalDate endDate
  );

  void deleteByEventCode(String eventCode);
}
