package com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.venue;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.event.EventDTO;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VenueWithEventsDTO {
  private VenueDTO venueInfo;
  private List<EventDTO> events;
}
