package com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.location;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationDTO {
  @NotBlank
  private String locationId;

  @NotBlank
  private String address;
}
