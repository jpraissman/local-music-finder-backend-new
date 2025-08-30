package com.thelocalmusicfinder.thelocalmusicfinderbackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
public class EventController {

  public EventController() {

  }

  @GetMapping
  public String getEvents() {
    return "These are all the events 2";
  }
}
