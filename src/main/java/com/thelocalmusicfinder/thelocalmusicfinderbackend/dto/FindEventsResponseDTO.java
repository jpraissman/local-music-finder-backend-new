package com.thelocalmusicfinder.thelocalmusicfinderbackend.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FindEventsResponseDTO {
  List<EventDTO> events;
}
