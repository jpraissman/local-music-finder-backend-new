package com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.band.BasicBandInfo;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.UpsertEventRequestDTO;

import org.springframework.stereotype.Component;

@Component
public class BandMapper {

  public BasicBandInfo toBasicBand(UpsertEventRequestDTO request) {
    return BasicBandInfo.builder()
            .bandName(request.getBandName())
            .bandType(request.getBandType())
            .tributeBandName(request.getTributeBandName())
            .genres(request.getGenres())
            .facebookUrl(request.getFacebookUrl())
            .instagramUrl(request.getInstagramUrl())
            .websiteUrl(request.getWebsiteUrl())
            .build();
  }

}
