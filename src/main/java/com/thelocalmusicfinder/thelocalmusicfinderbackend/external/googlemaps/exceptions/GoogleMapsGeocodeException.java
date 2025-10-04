package com.thelocalmusicfinder.thelocalmusicfinderbackend.external.googlemaps.exceptions;

public class GoogleMapsGeocodeException extends RuntimeException{
  public GoogleMapsGeocodeException(String message) {
    super(message);
  }
}
