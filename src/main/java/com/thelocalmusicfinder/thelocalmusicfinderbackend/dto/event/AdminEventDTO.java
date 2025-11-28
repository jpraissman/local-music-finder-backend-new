package com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AdminEventDTO extends EventDTO {
  private String eventCode;
}
