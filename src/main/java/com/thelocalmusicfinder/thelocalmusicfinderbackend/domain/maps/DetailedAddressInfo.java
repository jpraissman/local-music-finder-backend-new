package com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.maps;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
@AllArgsConstructor
public class DetailedAddressInfo {
  private final Coordinates coordinates;
  private final String formattedAddress;
  private final String town;
  private final String county;
}
