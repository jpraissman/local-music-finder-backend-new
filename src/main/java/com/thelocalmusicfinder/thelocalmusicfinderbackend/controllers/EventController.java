package com.thelocalmusicfinder.thelocalmusicfinderbackend.controllers;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.event.EventDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.event.FindEventsResponseDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.event.UpsertEventRequestDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.event.CreateEventResponseDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers.EventMapper;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Event;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.services.EventService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
  private final EventService eventService;
  private final EventMapper eventMapper;

  @PostMapping
  public ResponseEntity<CreateEventResponseDTO> createEvent(@Valid @RequestBody UpsertEventRequestDTO payload) {
    String createdEventCode = eventService.createEvent(payload);
    CreateEventResponseDTO response = new CreateEventResponseDTO(createdEventCode);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/{eventCode}")
  public ResponseEntity<UpsertEventRequestDTO> getEvent(@PathVariable("eventCode") String eventCode) {
    Event event = eventService.getEvent(eventCode);
    UpsertEventRequestDTO response = eventMapper.toUpsertEventRequestDTO(event);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PutMapping("/{eventCode}")
  public ResponseEntity<Void> editEvent(@PathVariable("eventCode") String eventCode,
                                        @Valid @RequestBody UpsertEventRequestDTO payload) {
    eventService.editEvent(payload, eventCode);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{eventCode}")
  public ResponseEntity<Void> deleteEvent(@PathVariable("eventCode") String eventCode) {
    eventService.deleteEvent(eventCode);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/find")
  public ResponseEntity<FindEventsResponseDTO> findEvents(
          @RequestParam String address,
          @RequestParam(required = false, defaultValue = "100") int distance,
          @RequestParam(required = false, defaultValue = "America/New_York") String timezone) {
    List<Event> rawEvents = eventService.findEvents(address, distance, timezone);

    List<EventDTO> finalEvents = new ArrayList<>();
    for (Event event : rawEvents) {
      finalEvents.add(eventMapper.toEventDTO(event));
    }
    FindEventsResponseDTO response = new FindEventsResponseDTO(finalEvents);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
