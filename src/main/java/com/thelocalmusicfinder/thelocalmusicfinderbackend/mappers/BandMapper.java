package com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.band.BasicBandInfo;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.band.BandDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.event.UpsertEventRequestDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Band;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
public class BandMapper {

  public BasicBandInfo toBasicBand(UpsertEventRequestDTO request) {
    return BasicBandInfo.builder()
            .bandName(request.getBandName())
            .bandType(request.getBandType())
            .tributeBandName(request.getTributeBandName())
            .genres(request.getGenres())
            .facebookUrl(request.getBandFacebookUrl())
            .instagramUrl(request.getBandInstagramUrl())
            .websiteUrl(request.getBandWebsiteUrl())
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
