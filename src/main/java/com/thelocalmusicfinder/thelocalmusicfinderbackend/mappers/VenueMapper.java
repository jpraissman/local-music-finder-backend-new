package com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.BasicVenueInfo;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.UpsertEventRequestDTO;

import org.springframework.stereotype.Component;

@Component
public class VenueMapper {

  public BasicVenueInfo toBasicVenueInfo(UpsertEventRequestDTO request) {
    return BasicVenueInfo.builder()
            .venueName(request.getVenueName())
            .address(request.getAddress())
            .facebookUrl(request.getFacebookUrl())
            .instagramUrl(request.getInstagramUrl())
            .websiteUrl(request.getWebsiteUrl())
            .phoneNumber(request.getVenuePhone())
            .build();
  }

}
