package com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateEventResponseDTO {
  @NotBlank
  @Size(min = 1, max = 20)
  private String eventCode;
}
