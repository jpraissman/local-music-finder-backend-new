package com.thelocalmusicfinder.thelocalmusicfinderbackend.external.googlemaps;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.external.googlemaps.exceptions.GoogleMapsGeocodeException;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.external.googlemaps.types.GoogleMapsGeocodeResponse;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.services.LoggerService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class GoogleMapsService {

  @Value("${google.maps.api.key}")
  private String googleMapsApiKey;

  private final WebClient webClient;
  private final LoggerService logger;

  public GoogleMapsService(WebClient.Builder builder, LoggerService logger) {
    this.webClient = builder.baseUrl("https://maps.googleapis.com/maps/api").build();
    this.logger = logger;
  }

  public GoogleMapsGeocodeResponse getGeocodeResponseByPlaceId(String placeId)
          throws GoogleMapsGeocodeException {
    logger.info("Querying Google Maps Geocode API with place_id " + placeId);
    String queryParam = "?place_id=" + placeId;
    return getGeocodeResponseHelper(queryParam);
  }

  public GoogleMapsGeocodeResponse getGeocodeResponseByAddress(String address)
          throws GoogleMapsGeocodeException {
    String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
    logger.info("Querying Google Maps Geocode API with address " + address
            + " and encoded address " + encodedAddress);
    String queryParam = "?address=" + encodedAddress;
    return getGeocodeResponseHelper(queryParam);
  }

  private GoogleMapsGeocodeResponse getGeocodeResponseHelper(String queryParam)
          throws GoogleMapsGeocodeException {
    try {
      String uri = "/geocode/json" +  queryParam + "&key=" + googleMapsApiKey;
      return webClient.get()
              .uri(uri)
              .retrieve()
              .bodyToMono(GoogleMapsGeocodeResponse.class)
              .block();
    } catch (WebClientResponseException e) {
      logger.error("GoogleMaps geocode api failed for queryParam "
               + queryParam + " with status code " + e.getStatusCode()
              + ".\n Response Body: " + e.getResponseBodyAsString());
      throw new GoogleMapsGeocodeException("GoogleMaps geocode api failed.");
    } catch (Exception e) {
      logger.error("GoogleMaps geocode api failed for queryParam "
              + queryParam + ". Error: " + e.getMessage());
      throw new GoogleMapsGeocodeException("GoogleMaps geocode api failed.");
    }
  }
}
