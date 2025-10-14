package com.thelocalmusicfinder.thelocalmusicfinderbackend.errors.exceptions;

public class VenueNotFound extends RuntimeException {
  public VenueNotFound(String message) {
    super(message);
  }
}
