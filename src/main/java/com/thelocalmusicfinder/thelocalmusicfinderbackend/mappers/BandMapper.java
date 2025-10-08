package com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.band.BasicBandInfo;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.BandDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.UpsertEventRequestDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Band;

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

  public BandDTO toBandDTO(Band band) {
    return BandDTO.builder()
            .id(band.getId())
            .bandName(band.getBandName())
            .bandType(band.getBandType())
            .tributeBandName(band.getTributeBandName())
            .genres(band.getGenres())
            .facebookUrl(band.getFacebookUrl())
            .instagramUrl(band.getInstagramUrl())
            .websiteUrl(band.getWebsiteUrl())
            .youtubeVideoIds(band.getYoutube_video_ids()).build();
  }

}
