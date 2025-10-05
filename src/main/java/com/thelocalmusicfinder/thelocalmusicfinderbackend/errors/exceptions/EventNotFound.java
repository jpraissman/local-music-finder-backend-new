package com.thelocalmusicfinder.thelocalmusicfinderbackend.errors.exceptions;

public class EventNotFound extends RuntimeException {
  public EventNotFound(String message) {
    super(message);
  }
}
