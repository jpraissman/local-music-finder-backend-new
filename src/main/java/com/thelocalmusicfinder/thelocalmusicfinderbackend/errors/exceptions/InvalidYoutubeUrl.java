package com.thelocalmusicfinder.thelocalmusicfinderbackend.errors.exceptions;

public class InvalidYoutubeUrl extends RuntimeException {
  public InvalidYoutubeUrl(String message) {
    super(message);
  }
}
