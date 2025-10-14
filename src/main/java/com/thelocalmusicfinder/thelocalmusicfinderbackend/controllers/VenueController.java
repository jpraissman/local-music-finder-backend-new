package com.thelocalmusicfinder.thelocalmusicfinderbackend.controllers;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.venue.GetVenuesDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.venue.VenueDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.venue.VenueWithEventsDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers.TopLevelVenueMapper;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers.VenueMapper;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Venue;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.services.VenueService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/venues")
@RequiredArgsConstructor
public class VenueController {
  private final VenueService venueService;
  private final VenueMapper venueMapper;
  private final TopLevelVenueMapper topLevelVenueMapper;

  @GetMapping
  public ResponseEntity<GetVenuesDTO> getVenues() {
    List<Venue> venues = venueService.getAllVenues();

    Map<String, VenueDTO> venueResults = new HashMap<>();
    for (Venue venue : venues) {
      venueResults.put(venue.getVenueName(), venueMapper.toVenueDTO(venue));
    }

    return ResponseEntity.status(HttpStatus.OK).body(new GetVenuesDTO(venueResults));
  }

  @GetMapping("/{id}")
  public ResponseEntity<VenueWithEventsDTO> getVenue(@PathVariable Long id) {
    Venue venue = venueService.getVenue(id);
    return ResponseEntity.status(HttpStatus.OK).body(topLevelVenueMapper.toVenueWithEventsDTO(venue));
  }
}
