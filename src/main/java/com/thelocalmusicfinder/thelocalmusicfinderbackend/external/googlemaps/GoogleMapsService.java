package com.thelocalmusicfinder.thelocalmusicfinderbackend.external.googlemaps;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.external.googlemaps.exceptions.GoogleMapsGeocodeException;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.external.googlemaps.types.GoogleMapsGeocodeResponse;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.services.LoggerService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

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

  public GoogleMapsGeocodeResponse getGoogleMapsGeocodeResponse(String address)
          throws GoogleMapsGeocodeException {
    logger.info("Querying Google Maps Geocode API with address " + address);
    String queryParams = "?address=" + address + "&key=" + googleMapsApiKey;
    String uri = "/geocode/json" +  queryParams;
    try {
      return webClient.get()
              .uri(uri)
              .retrieve()
              .bodyToMono(GoogleMapsGeocodeResponse.class)
              .block();
    } catch (WebClientResponseException e) {
      logger.error("GoogleMaps geocode api failed for address "
               + address + " with status code " + e.getStatusCode()
              + ".\n Response Body: " + e.getResponseBodyAsString());
      throw new GoogleMapsGeocodeException("GoogleMaps geocode api failed.");
    } catch (Exception e) {
      logger.error("GoogleMaps geocode api failed for address "
              + address + ". Error: " + e.getMessage());
      throw new GoogleMapsGeocodeException("GoogleMaps geocode api failed.");
    }
  }
}
