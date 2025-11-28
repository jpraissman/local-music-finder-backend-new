package com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.event;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MultiAdminEventsResponseDTO {
  List<AdminEventDTO> events;
}
