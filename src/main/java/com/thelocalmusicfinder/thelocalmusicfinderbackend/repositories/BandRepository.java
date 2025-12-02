package com.thelocalmusicfinder.thelocalmusicfinderbackend.repositories;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Band;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BandRepository extends JpaRepository<Band, Long> {
  Optional<Band> findByBandName(String bandName);

  List<Band> findByBandNameContainingIgnoreCase(String bandNameQuery);

  @Modifying
  @Query("DELETE FROM Band b WHERE b.events IS EMPTY")
  void deleteAllWithNoEvents();
}
