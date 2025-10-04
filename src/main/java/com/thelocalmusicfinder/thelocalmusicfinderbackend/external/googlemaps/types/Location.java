package com.thelocalmusicfinder.thelocalmusicfinderbackend.external.googlemaps.types;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Location {
  private Double lat;
  private Double lng;
}
