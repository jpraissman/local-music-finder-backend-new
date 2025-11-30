package com.thelocalmusicfinder.thelocalmusicfinderbackend.errors;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.errors.exceptions.EmailSendException;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.errors.exceptions.LocationQueryException;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.errors.exceptions.EventNotFound;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.services.EmailService;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.services.LoggerService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
  private final LoggerService logger;
  private final EmailService  emailService;

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception exception, HttpServletRequest request) {
    this.logErrorAndEmail("Generic Exception: " + exception.getMessage(), request);

    ErrorResponse error = new ErrorResponse("INTERNAL_SERVER_ERROR", "Internal Server Error");
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(LocationQueryException.class)
  public ResponseEntity<ErrorResponse> handleAddressCoordinatesException(LocationQueryException exception, HttpServletRequest request) {
    this.logErrorAndEmail("Location Query Exception: " + exception.getMessage(), request);

    ErrorResponse error = new ErrorResponse("ADDRESS_ERROR", "The given address could not be processed.");
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(EventNotFound.class)
  public ResponseEntity<ErrorResponse> handleEventNotFound(EventNotFound exception, HttpServletRequest request) {
    this.logErrorAndEmail("Event Not Found: " + exception.getMessage(), request);

    ErrorResponse error = new ErrorResponse("EVENT_NOT_FOUND", "The given event could not be found.");
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  // Handles @Valid on @RequestBody
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
    this.logErrorAndEmail("Validation Exception: " + ex.getMessage(), request);

    Map<String, String> errors = new HashMap<>();

    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.put(error.getField(), error.getDefaultMessage());
    }

    return ResponseEntity.badRequest().body(errors);
  }

  private void logErrorAndEmail(String customMessage, HttpServletRequest request) {
    String message = String.format(
            "{ path: \"%s\", method: \"%s\", remoteIp: \"%s\", customMessage: \"%s\" }",
            request.getRequestURI(),
            request.getMethod(),
            request.getRemoteAddr(),
            customMessage
    );
    logger.error(message);

    try {
      emailService.sendErrorEmail("ERROR: An error occurred in the backend (See details)", "<p>" + message + "</p>");
    } catch (EmailSendException exception) {
      logger.error("Error occurred when trying to send generic email error message " + exception.getMessage());
    }
  }
}
