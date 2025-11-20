package com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.BasicVenueInfo;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.event.EventDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.event.UpsertEventRequestDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.venue.VenueDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.venue.VenueWithEventsDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Event;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Venue;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

@Component
public class VenueMapper {

  public BasicVenueInfo toBasicVenueInfo(UpsertEventRequestDTO request) {
    return BasicVenueInfo.builder()
            .venueName(request.getVenueName())
            .locationId(request.getLocationId())
            .facebookUrl(request.getVenueFacebookUrl())
            .instagramUrl(request.getVenueInstagramUrl())
            .websiteUrl(request.getVenueWebsiteUrl())
            .phoneNumber(request.getVenuePhone())
            .build();
  }

  public VenueDTO toVenueDTO(Venue venue) {
    return VenueDTO.builder()
            .id(venue.getId())
            .venueName(venue.getVenueName())
            .address(venue.getLocation().getFormattedAddress())
            .town(venue.getLocation().getTown())
            .phoneNumber(venue.getPhoneNumber())
            .facebookUrl(venue.getFacebookUrl())
            .instagramUrl(venue.getInstagramUrl())
            .websiteUrl(venue.getWebsiteUrl()).build();
  }

}
