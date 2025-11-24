package com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.venue.VenueDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.band.BandDTO;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventDTO {
  private Long id;

  private VenueDTO venue;

  private BandDTO band;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate eventDate;

  @JsonFormat(pattern = "HH:mm")
  private LocalTime startTime;

  @JsonFormat(pattern = "HH:mm")
  private LocalTime endTime;

  private Double coverCharge;

  private String additionalInfo;

  private Double distanceInMiles;
}
