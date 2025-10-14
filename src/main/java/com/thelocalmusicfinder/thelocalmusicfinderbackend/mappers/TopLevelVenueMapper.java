package com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.event.EventDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.venue.VenueWithEventsDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Event;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Venue;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TopLevelVenueMapper {
  private final EventMapper eventMapper;
  private final VenueMapper venueMapper;

  public VenueWithEventsDTO toVenueWithEventsDTO(Venue venue) {
    List<EventDTO> venueEvents = new ArrayList<>();
    for (Event event : venue.getEvents()) {
      venueEvents.add(eventMapper.toEventDTO(event));
    }

    return VenueWithEventsDTO.builder()
            .venueInfo(venueMapper.toVenueDTO(venue))
            .events(venueEvents).build();
  }
}
