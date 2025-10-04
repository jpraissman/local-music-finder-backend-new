package com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.maps;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class DetailedAddressInfo {
  private final Coordinates coordinates;
  private final String formattedAddress;
  private final String town;
  private final String county;
}
