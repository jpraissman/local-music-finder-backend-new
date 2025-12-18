package com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.location;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExtendedLocationDTO {
  private String locationId;
  private String formattedAddress;
  private String town;
  private String county;
}
