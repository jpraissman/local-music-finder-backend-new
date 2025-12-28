package com.thelocalmusicfinder.thelocalmusicfinderbackend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

  @GetMapping("/favicon.ico")
  public ResponseEntity<Void> favicon() {
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/")
  public ResponseEntity<Void> index() {
    return ResponseEntity.notFound().build();
  }
}
