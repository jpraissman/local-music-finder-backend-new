package com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.BandDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.EventDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.VenueDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Event;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventMapper {
  private final BandMapper bandMapper;
  private final VenueMapper venueMapper;

  public EventDTO toEventDTO(Event event) {
    BandDTO bandDTO = bandMapper.toBandDTO(event.getBand());
    VenueDTO venueDTO = venueMapper.toVenueDTO(event.getVenue());
    return EventDTO.builder()
            .id(event.getId())
            .venue(venueDTO)
            .band(bandDTO)
            .eventDate(event.getEventDate())
            .startTime(event.getStartTime())
            .endTime(event.getEndTime())
            .coverCharge(event.getCoverCharge())
            .additionalInfo(event.getAdditionalInfo()).build();
  }
}
