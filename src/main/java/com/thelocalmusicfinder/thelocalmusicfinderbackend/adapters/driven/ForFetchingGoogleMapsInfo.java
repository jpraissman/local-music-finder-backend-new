package com.thelocalmusicfinder.thelocalmusicfinderbackend.adapters.driven;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.maps.Coordinates;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.maps.DetailedAddressInfo;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.errors.exceptions.AddressCoordinatesException;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.external.googlemaps.GoogleMapsService;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.external.googlemaps.exceptions.GoogleMapsGeocodeException;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.external.googlemaps.types.AddressComponent;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.external.googlemaps.types.GoogleMapsGeocodeResponse;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.ports.driven.ForFetchingMapInfo;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.services.LoggerService;

import org.springframework.stereotype.Component;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ForFetchingGoogleMapsInfo implements ForFetchingMapInfo {
  private final GoogleMapsService googleMapsService;
  private final LoggerService logger;

  @Override
  public Coordinates getAddressCoordinates(String address) {
    GoogleMapsGeocodeResponse response = getGoogleMapsGeocodeResponse(address);
    return getAddressCoordinatesHelper(address, response);
  }

  private Coordinates getAddressCoordinatesHelper(String address, GoogleMapsGeocodeResponse response) {
    try {
      logger.info("Getting address coordinates for address " + address
              + ". Response: " + response.toString());

      if (response.getResults().isEmpty()) {
        logger.error("GoogleMapsGeocodeResponse is empty for address " + address
                + ". Response: " + response.toString());
        throw new AddressCoordinatesException("GoogleMapsGeocodeResponse is empty");
      }
      if (response.getResults().size() > 1) {
        logger.warn("GoogleMapsGeocodeResponse is more than 1 result for address " + address
                + ". Response: " + response.toString());
      }

      Double lat = response.getResults().getFirst().getGeometry().getLocation().getLat();
      Double lng = response.getResults().getFirst().getGeometry().getLocation().getLng();
      return new Coordinates(lat, lng);
    } catch (AddressCoordinatesException e) {
      throw e;
    } catch (Exception e) {
      logger.error("Error fetching Coordinates for address " + address
              + ". Error: " + e.getMessage());
      throw new AddressCoordinatesException("Error fetching Coordinates for address");
    }
  }

  @Override
  public DetailedAddressInfo getDetailedAddressInfo(String address) {
    GoogleMapsGeocodeResponse response = getGoogleMapsGeocodeResponse(address);
    Coordinates coordinates = getAddressCoordinatesHelper(address, response);

    // Get the rest of the address information
    try {
      String formattedAddress = response.getResults().getFirst().getFormattedAddress();

      String town = "Unknown";
      String county = "Unknown";
      List<AddressComponent> addressComponents = response.getResults().getFirst().getAddressComponents();
      for (AddressComponent addressComponent : addressComponents) {
        if (addressComponent.getTypes().contains("locality")) {
          town = addressComponent.getLongName();
        }
        if (addressComponent.getTypes().contains("administrative_area_level_2")) {
          county = addressComponent.getLongName();
        }
      }

      return DetailedAddressInfo.builder()
              .coordinates(coordinates)
              .formattedAddress(formattedAddress)
              .town(town)
              .county(county).build();
    } catch (Exception e) {
      logger.warn("Error fetching detailed address info for address " + address
      + ". Error: " + e.getMessage() + ". Setting unknown fields to Unknown.");
      return DetailedAddressInfo.builder()
              .coordinates(coordinates)
              .formattedAddress("Unknown")
              .town("Unknown")
              .county("Unknown")
              .build();
    }
  }

  private GoogleMapsGeocodeResponse getGoogleMapsGeocodeResponse(String address) {
    try {
      return googleMapsService.getGoogleMapsGeocodeResponse(address);
    } catch  (GoogleMapsGeocodeException e) {
      logger.error("Error getting GoogleMapsGeocodeResponse from GoogleMapsService for address "
              + address + ". Error message: " + e.getMessage());
      throw new AddressCoordinatesException("Error fetching coordinates for address");
    }
  }
}
