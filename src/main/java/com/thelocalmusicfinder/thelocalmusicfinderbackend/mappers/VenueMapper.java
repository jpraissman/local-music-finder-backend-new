package com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.BasicVenueInfo;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.UpsertEventRequestDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.VenueDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Venue;

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

  public VenueDTO toVenueDTO(Venue venue) {
    return VenueDTO.builder()
            .id(venue.getId())
            .venueName(venue.getVenueName())
            .address(venue.getAddress())
            .phoneNumber(venue.getPhoneNumber())
            .facebookUrl(venue.getFacebookUrl())
            .instagramUrl(venue.getInstagramUrl())
            .websiteUrl(venue.getWebsiteUrl()).build();
  }

}
