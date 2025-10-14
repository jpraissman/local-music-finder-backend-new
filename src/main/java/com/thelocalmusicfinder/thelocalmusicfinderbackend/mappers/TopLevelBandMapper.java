package com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.band.BandWithEventsDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.event.EventDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Band;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Event;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TopLevelBandMapper {
  private final BandMapper bandMapper;
  private final EventMapper eventMapper;

  public BandWithEventsDTO toBandWithEventsDTO(Band band) {
    List<EventDTO> bandEvents = new ArrayList<>();
    for (Event event : band.getEvents()) {
      bandEvents.add(eventMapper.toEventDTO(event));
    }

    return BandWithEventsDTO.builder()
            .bandInfo(bandMapper.toBandDTO(band))
            .events(bandEvents).build();
  }
}
