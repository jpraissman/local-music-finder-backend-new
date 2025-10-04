package com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.band;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BasicBandInfo {
  private final String bandName;
  private final BandType bandType;
  private final String tributeBandName;
  private final List<Genre> genres;
  private final String facebookUrl;
  private final String instagramUrl;
  private final String websiteUrl;
}
