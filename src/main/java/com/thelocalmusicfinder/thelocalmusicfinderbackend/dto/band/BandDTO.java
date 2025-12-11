package com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.band;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.band.BandType;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.band.Genre;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BandDTO {
  @NotNull
  private Long id;

  @NotBlank
  private String bandName;

  @NotNull
  private BandType bandType;

  private String tributeBandName;

  @NotNull
  @Size(min = 1)
  private List<Genre> genres;

  private String facebookUrl;

  private String instagramUrl;

  private String websiteUrl;

  @NotNull
  private List<String> youtubeVideoIds;
}
