package com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.venue;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetVenuesDTO {
  Map<String, VenueDTO> venues;
}
