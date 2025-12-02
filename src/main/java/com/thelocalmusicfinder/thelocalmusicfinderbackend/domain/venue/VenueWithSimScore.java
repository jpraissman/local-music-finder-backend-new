package com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.venue;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Venue;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VenueWithSimScore {
  private final Venue venue;
  private final double simScore;
}
