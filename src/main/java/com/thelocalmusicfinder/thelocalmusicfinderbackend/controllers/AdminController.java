package com.thelocalmusicfinder.thelocalmusicfinderbackend.controllers;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.band.BandDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.event.AdminEventDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.event.MultiAdminEventsResponseDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.venue.VenueDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers.CsvMapper;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers.EventMapper;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Band;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Event;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Venue;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.repositories.BandRepository;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.repositories.EventRepository;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.repositories.VenueRepository;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.services.BandService;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.services.EventService;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.services.VenueService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
// Uses the AdminKeyFilter to validate that caller is an admin for all of these requests
public class AdminController {
  private final VenueService venueService;
  private final BandService bandService;
  private final CsvMapper csvMapper;
  private final EventRepository eventRepository;
  private final BandRepository bandRepository;
  private final VenueRepository venueRepository;
  private final EventService eventService;
  private final EventMapper eventMapper;

  @GetMapping("/validate")
  public ResponseEntity<Void> validateAdmin() {
    return ResponseEntity.ok().build();
  }

  @GetMapping("/event/csv")
  public ResponseEntity<String> getEventsCsv() {
    List<Event> allEvents = eventRepository.findAll();
    String csv = csvMapper.toEventsCsv(allEvents);
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"events.csv\"").body(csv);
  }

  @GetMapping("/band/csv")
  public ResponseEntity<String> getBandsCsv() {
    List<Band> allBands = bandRepository.findAll();
    String csv = csvMapper.toBandsCsv(allBands);
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"bands.csv\"").body(csv);
  }

  @GetMapping("/venue/csv")
  public ResponseEntity<String> getVenuesCsv() {
    List<Venue> allVenues = venueRepository.findAll();
    String csv = csvMapper.toVenuesCsv(allVenues);
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"venues.csv\"").body(csv);
  }

  @GetMapping("/event/all-future")
  public ResponseEntity<MultiAdminEventsResponseDTO> getAllFutureEvents(@RequestParam(required = false, defaultValue = "America/New_York") String timezone) {
    List<Event> events = eventService.getAllFutureEvents(timezone);
    List<AdminEventDTO> result = new ArrayList<AdminEventDTO>();
    for (Event event : events) {
      result.add(eventMapper.toAdminEventDTO(event));
    }
    return  ResponseEntity.ok().body(new MultiAdminEventsResponseDTO(result));
  }

  @PostMapping("/band/merge")
  public ResponseEntity<Void> mergeBands(@RequestParam() Long band1Id, @RequestParam() Long band2Id, @Valid @RequestBody BandDTO mergedBandInfo) {
    bandService.mergeBands(band1Id, band2Id, mergedBandInfo);
    return ResponseEntity.ok().build();
  }

//  @PutMapping("/venue/edit")
//  public ResponseEntity<Void> editVenue(@Valid @RequestBody VenueDTO venuePayload) {
//    venueService.editVenue(venuePayload);
//    return ResponseEntity.ok().build();
//  }

//  @PutMapping("band/edit")
//  public ResponseEntity<Void> editBand(@Valid @RequestBody BandDTO bandPayload) {
//    bandService.editBand(bandPayload);
//    return ResponseEntity.ok().build();
//  }


}
