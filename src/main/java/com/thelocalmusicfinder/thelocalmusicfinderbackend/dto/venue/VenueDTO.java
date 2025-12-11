package com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.venue;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.location.LocationDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VenueDTO {
  @NotNull()
  private Long id;

  @NotBlank()
  private String venueName;

  @NotNull()
  private LocationDTO location;

  private String town;

  private String county;

  private String phoneNumber;

  private String facebookUrl;

  private String instagramUrl;

  private String websiteUrl;
}
