package com.thelocalmusicfinder.thelocalmusicfinderbackend.dto;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.band.BandType;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.band.Genre;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BandDTO {
  private Long id;

  private String bandName;

  private BandType bandType;

  private String tributeBandName;

  private List<Genre> genres;

  private String facebookUrl;

  private String instagramUrl;

  private String websiteUrl;

  private List<String> youtubeVideoIds;
}
