package com.thelocalmusicfinder.thelocalmusicfinderbackend.services;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Event;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Location;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.ports.driven.ForFetchingMapInfo;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.repositories.LocationRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MapsService {
  private final ForFetchingMapInfo mapsService;
  private final LocationRepository locationRepository;

  public Location getLocationById(String locationId) {
    Optional<Location> optionalLocation = locationRepository.findById(locationId);
    if (optionalLocation.isPresent()) {
      return optionalLocation.get();
    }

    Location queriedLocation = mapsService.getLocationInfoById(locationId);
    locationRepository.save(queriedLocation);
    return queriedLocation;
  }

  public Location getLocationByAddress(String address) {
    Location location = mapsService.getLocationInfoByAddress(address);
    locationRepository.save(location);
    return location;
  }

  /**
   * Returns a list of events within {distance} miles of the given location
   * @param events events to filter
   * @param distance distance in miles
   * @param locationId locationId of location to search in
   * @return list of events within {distance} miles of the given location
   */
  public List<Event> filterEventsByDistance(List<Event> events, String locationId, int distance) {
    Location location = getLocationById(locationId);

    List<Event> filteredEvents = new ArrayList<>();
    for (Event event : events) {
      double approxDrivingDistance = approxDrivingDistance(
              event.getVenue().getLocation().getLatitude(),
              location.getLatitude(),
              event.getVenue().getLocation().getLongitude(),
              location.getLongitude());
      if (approxDrivingDistance <= distance) {
        filteredEvents.add(event);
      }
    }

    return filteredEvents;
  }

  private double approxDrivingDistance(double lat1, double lat2, double lon1, double lon2) {
    // Convert degrees to radians
    lon1 = Math.toRadians(lon1);
    lon2 = Math.toRadians(lon2);
    lat1 = Math.toRadians(lat1);
    lat2 = Math.toRadians(lat2);

    // Haversine formula
    double dlon = lon2 - lon1;
    double dlat = lat2 - lat1;
    double a = Math.pow(Math.sin(dlat / 2), 2)
            + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);

    double c = 2 * Math.asin(Math.sqrt(a));

    // Radius of Earth in miles
    double r = 3956;

    // Calculate the result (include scaling factor for more accurate driving distance)
    return c * r * 1.3;
  }
}
