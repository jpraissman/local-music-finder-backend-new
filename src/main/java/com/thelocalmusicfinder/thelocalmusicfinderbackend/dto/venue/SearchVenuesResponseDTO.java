package com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.venue;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchVenuesResponseDTO {
  List<VenueDTO> venues;
}
