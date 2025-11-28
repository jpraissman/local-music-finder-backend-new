package com.thelocalmusicfinder.thelocalmusicfinderbackend.controllers;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.location.LocationDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Location;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.services.MapsService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class LocationController {
  private final MapsService mapsService;

  @GetMapping("/{id}")
  public ResponseEntity<LocationDTO> getLocationById(@PathVariable String id) {
    Location location = this.mapsService.getLocationById(id);
    LocationDTO locationDTO = new LocationDTO(location.getLocationId(), location.getFormattedAddress());
    return ResponseEntity.ok(locationDTO);
  }
}
