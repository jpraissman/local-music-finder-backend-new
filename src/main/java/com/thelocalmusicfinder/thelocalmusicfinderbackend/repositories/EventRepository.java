package com.thelocalmusicfinder.thelocalmusicfinderbackend.repositories;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Event;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Venue;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
  Optional<Event> findByEventCode(String eventCode);

  List<Event> findByIdIn(List<Long> ids);

  List<Event> findByVenueAndEventDate(Venue venue, LocalDate date);

  List<Event> findByEventDateGreaterThanEqual(LocalDate startDate);

  List<Event> findByEventDateBetween(LocalDate startDate, LocalDate endDate);

  List<Event> findByEventDateBetweenAndVenue_Location_CountyIn(LocalDate startDate, LocalDate endDate, List<String> counties);

  void deleteByEventCode(String eventCode);
}
