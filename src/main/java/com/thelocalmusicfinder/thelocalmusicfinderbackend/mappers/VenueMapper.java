package com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.venue.BasicVenueInfo;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.event.UpsertEventRequestDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.location.LocationDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.venue.VenueDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Venue;

import org.springframework.stereotype.Component;

@Component
public class VenueMapper {

  public BasicVenueInfo toBasicVenueInfo(UpsertEventRequestDTO request) {
    return BasicVenueInfo.builder()
            .venueName(request.getVenueName())
            .locationId(request.getLocation().getLocationId())
            .facebookUrl(request.getVenueFacebookUrl())
            .instagramUrl(request.getVenueInstagramUrl())
            .websiteUrl(request.getVenueWebsiteUrl())
            .phoneNumber(request.getVenuePhone())
            .build();
  }

  public BasicVenueInfo toBasicVenueInfo(Venue venue) {
    return BasicVenueInfo.builder()
            .venueName(venue.getVenueName())
            .locationId(venue.getLocation().getLocationId())
            .facebookUrl(venue.getFacebookUrl())
            .instagramUrl(venue.getInstagramUrl())
            .websiteUrl(venue.getWebsiteUrl())
            .phoneNumber(venue.getPhoneNumber())
            .build();
  }

  public VenueDTO toVenueDTO(Venue venue) {
    return VenueDTO.builder()
            .id(venue.getId())
            .venueName(venue.getVenueName())
            .location(new LocationDTO(venue.getLocation().getLocationId(), venue.getLocation().getFormattedAddress()))
            .town(venue.getLocation().getTown())
            .phoneNumber(venue.getPhoneNumber())
            .facebookUrl(venue.getFacebookUrl())
            .instagramUrl(venue.getInstagramUrl())
            .websiteUrl(venue.getWebsiteUrl())
            .county(venue.getLocation().getCounty()).build();
  }

}
