package com.thelocalmusicfinder.thelocalmusicfinderbackend.ports.driven;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.maps.Coordinates;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.maps.DetailedAddressInfo;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.errors.exceptions.AddressCoordinatesException;

public interface ForFetchingMapInfo {

  /**
   * @param address address to query
   * @return Coordinates (lat and long) of the address
   * @throws AddressCoordinatesException if the coordinates could not be determined
   */
  Coordinates getAddressCoordinates(String address) throws AddressCoordinatesException;

  /**
   * @param address address to query
   * @return Detailed information about the address
   * @throws AddressCoordinatesException if the coordinates could not be determined. If other
   * fields can't be determined, they will be set to "Unknown"
   */
  DetailedAddressInfo getDetailedAddressInfo(String address) throws AddressCoordinatesException;

}
