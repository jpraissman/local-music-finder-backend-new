package com.thelocalmusicfinder.thelocalmusicfinderbackend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VenueDTO {
  private Long id;

  private String venueName;

  private String address;

  private String phoneNumber;

  private String facebookUrl;

  private String instagramUrl;

  private String websiteUrl;
}
