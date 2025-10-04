package com.thelocalmusicfinder.thelocalmusicfinderbackend.external.googlemaps.types;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class GoogleMapsGeocodeResponse {
  private List<GeocodeResults> results;
  private String status;

  @JsonProperty("error_message")
  private String errorMessage;
}

