package com.thelocalmusicfinder.thelocalmusicfinderbackend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoggerService {
  private static final Logger logger = LoggerFactory.getLogger(LoggerService.class);

  public void info(String message) {
    logger.info(message);
  }

  public void warn(String message) {
    logger.warn(message);
  }

  public void error(String message) {
    logger.error(message);
  }
}
