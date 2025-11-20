package com.thelocalmusicfinder.thelocalmusicfinderbackend.adapters.driven;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.errors.exceptions.LocationQueryException;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.external.googlemaps.GoogleMapsService;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.external.googlemaps.exceptions.GoogleMapsGeocodeException;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.external.googlemaps.types.AddressComponent;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.external.googlemaps.types.GoogleMapsGeocodeResponse;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Location;
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
  public Location getLocationInfoById(String locationId) throws LocationQueryException {
    return getLocationInfo(locationId, true);
  }

  @Override
  public Location getLocationInfoByAddress(String address) throws LocationQueryException {
    return getLocationInfo(address, false);
  }

  private Location getLocationInfo(String input, boolean isLocationId) {
    try {
      GoogleMapsGeocodeResponse geocodeResponse = getGoogleMapsGeocodeResponse(input, isLocationId);

      logger.info("Got the following geocodeResponse from input=" + input + ": " + geocodeResponse.toString());

      if (geocodeResponse.getResults().isEmpty()) {
        logger.error("GoogleMapsGeocodeResponse is empty for input=" + input + ". Response: " + geocodeResponse.toString());
        throw new LocationQueryException("GoogleMapsGeocodeResponse is empty");
      }
      if (geocodeResponse.getResults().size() > 1) {
        logger.warn("GoogleMapsGeocodeResponse has more than 1 result for input=" + input + ". Response: " + geocodeResponse.toString());
      }

      String placeId = geocodeResponse.getResults().getFirst().getPlaceId();
      Double lat = geocodeResponse.getResults().getFirst().getGeometry().getLocation().getLat();
      Double lng = geocodeResponse.getResults().getFirst().getGeometry().getLocation().getLng();
      String formattedAddress = geocodeResponse.getResults().getFirst().getFormattedAddress();

      String town = null;
      String county = null;
      List<AddressComponent> addressComponents = geocodeResponse.getResults().getFirst().getAddressComponents();
      for (AddressComponent addressComponent : addressComponents) {
        if (addressComponent.getTypes().contains("locality")) {
          town = addressComponent.getLongName();
        }
        if (addressComponent.getTypes().contains("administrative_area_level_2")) {
          county = addressComponent.getLongName();
        }
      }

      return Location.builder()
              .locationId(placeId)
              .latitude(lat)
              .longitude(lng)
              .formattedAddress(formattedAddress)
              .town(town)
              .county(county).build();
    } catch (LocationQueryException e) {
      throw e;
    } catch (Exception e) {
      logger.error("Error getting location information for input=" + input + ". Error: " + e.getMessage());
      throw new LocationQueryException("Error fetching location information");
    }
  }

  private GoogleMapsGeocodeResponse getGoogleMapsGeocodeResponse(String input, boolean isLocationId) {
    try {
      if (isLocationId)
        return googleMapsService.getGeocodeResponseByPlaceId(input);
      else
        return googleMapsService.getGeocodeResponseByAddress(input);
    } catch  (GoogleMapsGeocodeException e) {
      String queryType = isLocationId ? "locationId" : "address";
      logger.error("Error getting GoogleMapsGeocodeResponse from GoogleMapsService for " + queryType + " " + input + ". Error message: " + e.getMessage());
      throw new LocationQueryException("Error fetching coordinates for " + queryType);
    }
  }
}
