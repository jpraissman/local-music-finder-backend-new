package com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.location.ExtendedLocationDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Location;

import org.springframework.stereotype.Component;

@Component
public class LocationMapper {
  public ExtendedLocationDTO toExtendedLocationDTO(Location location) {
    return ExtendedLocationDTO.builder()
            .locationId(location.getLocationId())
            .formattedAddress(location.getFormattedAddress())
            .county(location.getCounty())
            .town(location.getTown()).build();
  }
}
