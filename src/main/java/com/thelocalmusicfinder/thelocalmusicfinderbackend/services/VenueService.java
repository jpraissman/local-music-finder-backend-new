package com.thelocalmusicfinder.thelocalmusicfinderbackend.services;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.BasicVenueInfo;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.maps.DetailedAddressInfo;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Venue;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.repositories.VenueRepository;

import org.springframework.stereotype.Service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VenueService {

  private final VenueRepository venueRepository;
  private final MapsService mapsService;

  public Venue upsertVenue(BasicVenueInfo venueInfo, boolean includeUrls) {
    String venueName = venueInfo.getVenueName().trim();
    Optional<Venue> existingVenue = venueRepository.findByVenueName(venueName);

    if (existingVenue.isPresent()) {
      return this.updateVenue(existingVenue.get(), venueInfo, includeUrls);
    } else {
      return this.createVenue(venueInfo, includeUrls);
    }
  }

  private Venue updateVenue(Venue existingVenue, BasicVenueInfo updatedVenueInfo, boolean includeUrls) {
    existingVenue.setFacebookUrl(updatedVenueInfo.getFacebookUrl());
    existingVenue.setInstagramUrl(updatedVenueInfo.getInstagramUrl());
    existingVenue.setWebsiteUrl(updatedVenueInfo.getWebsiteUrl());
    existingVenue.setPhoneNumber(updatedVenueInfo.getPhoneNumber());
    return venueRepository.save(existingVenue);
  }

  private Venue createVenue(BasicVenueInfo venueInfo, boolean includeUrls) {
    String facebookUrl = includeUrls ? venueInfo.getFacebookUrl() : null;
    String instagramUrl = includeUrls ? venueInfo.getInstagramUrl() : null;
    String websiteUrl = includeUrls ? venueInfo.getWebsiteUrl() : null;
    DetailedAddressInfo addressInfo = mapsService.getDetailedAddressInfo(venueInfo.getAddress());

    Venue venue = Venue.builder()
            .venueName(venueInfo.getVenueName().trim())
            .address(addressInfo.getFormattedAddress())
            .county(addressInfo.getCounty())
            .town(addressInfo.getTown())
            .facebookUrl(facebookUrl)
            .instagramUrl(instagramUrl)
            .websiteUrl(websiteUrl)
            .phoneNumber(venueInfo.getPhoneNumber())
            .latitude(addressInfo.getCoordinates().getLatitude())
            .longitude(addressInfo.getCoordinates().getLongitude())
            .build();
    return venueRepository.save(venue);
  }
}
