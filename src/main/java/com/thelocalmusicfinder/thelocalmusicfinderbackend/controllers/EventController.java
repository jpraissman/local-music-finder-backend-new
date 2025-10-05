package com.thelocalmusicfinder.thelocalmusicfinderbackend.controllers;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.UpsertEventRequestDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.CreateEventResponseDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.services.EventService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
  private final EventService eventService;

  @PostMapping
  public ResponseEntity<CreateEventResponseDTO> createEvent(@Valid @RequestBody UpsertEventRequestDTO payload) {
    String createdEventCode = eventService.createEvent(payload);
    CreateEventResponseDTO response = new CreateEventResponseDTO(createdEventCode);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PutMapping("/{eventCode}")
  public ResponseEntity<Void> editEvent(@PathVariable("eventCode") String eventCode,
                                        @Valid @RequestBody UpsertEventRequestDTO payload) {
    eventService.editEvent(payload, eventCode);
    return ResponseEntity.ok().build();
  }
}
