package com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.maps;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Coordinates {
  private final Double latitude;
  private final Double longitude;
}
