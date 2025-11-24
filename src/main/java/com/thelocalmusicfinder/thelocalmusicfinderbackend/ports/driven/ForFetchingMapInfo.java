package com.thelocalmusicfinder.thelocalmusicfinderbackend.ports.driven;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.errors.exceptions.LocationQueryException;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Location;

public interface ForFetchingMapInfo {

  /**
   * Returns location information about the given locationId.
   * Will set town and county to null if it could not determine those values.
   * @throws LocationQueryException if the coordinates or formattedAddress could not be determined
   */
  Location getLocationInfoById(String locationId) throws LocationQueryException;
}
