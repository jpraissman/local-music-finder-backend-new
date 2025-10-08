package com.thelocalmusicfinder.thelocalmusicfinderbackend.dto;

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

  private LocalDate eventDate;

  private LocalTime startTime;

  private LocalTime endTime;

  private Double coverCharge;

  private String additionalInfo;
}
