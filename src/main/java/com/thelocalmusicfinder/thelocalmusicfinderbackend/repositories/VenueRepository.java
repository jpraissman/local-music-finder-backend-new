package com.thelocalmusicfinder.thelocalmusicfinderbackend.repositories;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Location;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Venue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface VenueRepository extends JpaRepository<Venue, Long> {
  Optional<Venue> findByVenueNameAndLocation(String venueName, Location location);

  @Modifying
  @Query("DELETE FROM Venue v WHERE v.events IS EMPTY")
  void deleteAllWithNoEvents();
}
