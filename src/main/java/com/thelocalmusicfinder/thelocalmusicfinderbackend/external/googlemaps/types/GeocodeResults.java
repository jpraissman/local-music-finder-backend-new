package com.thelocalmusicfinder.thelocalmusicfinderbackend.external.googlemaps.types;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class GeocodeResults {
  @JsonProperty("address_components")
  private List<AddressComponent> addressComponents;

  @JsonProperty("formatted_address")
  private String formattedAddress;

  private Geometry geometry;

  @JsonProperty("place_id")
  private String placeId;
}
