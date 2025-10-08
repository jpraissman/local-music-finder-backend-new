package com.thelocalmusicfinder.thelocalmusicfinderbackend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BasicVenueInfo {
  private final String venueName;
  private final String address;
  private final String facebookUrl;
  private final String instagramUrl;
  private final String websiteUrl;
  private final String phoneNumber;
}
