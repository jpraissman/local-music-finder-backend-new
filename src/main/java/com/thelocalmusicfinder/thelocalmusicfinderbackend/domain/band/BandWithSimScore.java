package com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.band;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Band;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BandWithSimScore {
  private final Band band;
  private final double simScore;
}
