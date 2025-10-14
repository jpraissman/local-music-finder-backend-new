package com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.band;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.event.EventDTO;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BandWithEventsDTO {
  private BandDTO bandInfo;
  private List<EventDTO> events;
}
