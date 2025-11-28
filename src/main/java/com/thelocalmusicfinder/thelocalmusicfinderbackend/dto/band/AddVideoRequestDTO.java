package com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.band;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddVideoRequestDTO {
  @NotBlank
  private String youtubeUrl;
}
