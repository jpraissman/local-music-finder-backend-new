package com.thelocalmusicfinder.thelocalmusicfinderbackend.repositories;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Band;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BandRepository extends JpaRepository<Band, Long> {
  Optional<Band> findByBandName(String bandName);
}
