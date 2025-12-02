package com.thelocalmusicfinder.thelocalmusicfinderbackend.controllers;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.venue.SearchVenuesResponseDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.venue.VenueDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.venue.VenueWithEventsDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers.TopLevelVenueMapper;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers.VenueMapper;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Venue;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.services.VenueService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/venues")
@RequiredArgsConstructor
public class VenueController {
  private final VenueService venueService;
  private final VenueMapper venueMapper;
  private final TopLevelVenueMapper topLevelVenueMapper;

  @GetMapping("/search/{venueNameQuery}")
  public ResponseEntity<SearchVenuesResponseDTO> searchVenues(@PathVariable String venueNameQuery) {
    List<Venue> venues = venueService.searchVenues(venueNameQuery);

    List<VenueDTO> venueDTOs = new ArrayList<>();
    for (Venue venue : venues) {
      venueDTOs.add(venueMapper.toVenueDTO(venue));
    }

    return ResponseEntity.status(HttpStatus.OK).body(new SearchVenuesResponseDTO(venueDTOs));
  }

  @GetMapping("/{id}")
  public ResponseEntity<VenueWithEventsDTO> getVenue(@PathVariable Long id) {
    Venue venue = venueService.getVenue(id);
    return ResponseEntity.status(HttpStatus.OK).body(topLevelVenueMapper.toVenueWithEventsDTO(venue));
  }
}
