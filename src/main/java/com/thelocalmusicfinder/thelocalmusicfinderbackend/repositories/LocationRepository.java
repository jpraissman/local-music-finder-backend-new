package com.thelocalmusicfinder.thelocalmusicfinderbackend.repositories;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Location;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, String> { }
