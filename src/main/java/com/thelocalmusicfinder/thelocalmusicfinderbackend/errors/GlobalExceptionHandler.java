package com.thelocalmusicfinder.thelocalmusicfinderbackend.errors;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.errors.exceptions.AddressCoordinatesException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception exception) {
    ErrorResponse error = new ErrorResponse("INTERNAL_SERVER_ERROR", "Internal Server Error");
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(AddressCoordinatesException.class)
  public ResponseEntity<ErrorResponse> handleAddressCoordinatesException(AddressCoordinatesException exception) {
    ErrorResponse error = new ErrorResponse("ADDRESS_ERROR", "The given address could not be processed.");
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
