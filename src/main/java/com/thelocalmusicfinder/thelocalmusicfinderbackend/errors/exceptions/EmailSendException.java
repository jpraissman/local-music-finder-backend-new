package com.thelocalmusicfinder.thelocalmusicfinderbackend.errors.exceptions;

public class EmailSendException extends RuntimeException {
  public EmailSendException(String message) {
    super(message);
  }
}
