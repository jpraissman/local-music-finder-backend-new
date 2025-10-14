package com.thelocalmusicfinder.thelocalmusicfinderbackend.errors.exceptions;

public class BandNotFound extends RuntimeException {
  public BandNotFound(String message) {
    super(message);
  }
}
