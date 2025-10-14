package com.thelocalmusicfinder.thelocalmusicfinderbackend.services;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.BasicVenueInfo;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.maps.DetailedAddressInfo;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.errors.exceptions.VenueNotFound;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Venue;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.repositories.VenueRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VenueService {

  private final VenueRepository venueRepository;
  private final MapsService mapsService;
  private final LoggerService logger;
  private final EmailService emailService;

  public List<Venue> getAllVenues() {
    return venueRepository.findAll();
  }

  public Venue upsertVenue(BasicVenueInfo venueInfo) {
    // Check for existing venue based on name and coordinates
    String venueName = venueInfo.getVenueName().trim();
    DetailedAddressInfo addressInfo = mapsService.getDetailedAddressInfo(venueInfo.getAddress());
    Optional<Venue> existingVenue = venueRepository.findByVenueNameAndLatitudeAndLongitude(venueName,
            addressInfo.getCoordinates().getLatitude(), addressInfo.getCoordinates().getLongitude());

    if (existingVenue.isPresent()) {
      return this.updateVenue(existingVenue.get(), venueInfo);
    } else {
      return this.createVenue(venueInfo, addressInfo);
    }
  }

  private Venue updateVenue(Venue existingVenue, BasicVenueInfo updatedVenueInfo) {
    existingVenue.setFacebookUrl(updatedVenueInfo.getFacebookUrl());
    existingVenue.setInstagramUrl(updatedVenueInfo.getInstagramUrl());
    existingVenue.setWebsiteUrl(updatedVenueInfo.getWebsiteUrl());
    existingVenue.setPhoneNumber(updatedVenueInfo.getPhoneNumber());

    return venueRepository.save(existingVenue);
  }

  private Venue createVenue(BasicVenueInfo venueInfo, DetailedAddressInfo addressInfo) {
    Venue venue = Venue.builder()
            .venueName(venueInfo.getVenueName().trim())
            .address(addressInfo.getFormattedAddress())
            .county(addressInfo.getCounty())
            .town(addressInfo.getTown())
            .facebookUrl(venueInfo.getFacebookUrl())
            .instagramUrl(venueInfo.getInstagramUrl())
            .websiteUrl(venueInfo.getWebsiteUrl())
            .phoneNumber(venueInfo.getPhoneNumber())
            .latitude(addressInfo.getCoordinates().getLatitude())
            .longitude(addressInfo.getCoordinates().getLongitude())
            .build();

    if (addressInfo.getFormattedAddress() == null || addressInfo.getCounty() == null || addressInfo.getTown() == null) {
      logger.error("Venue with id " + venue.getId() + " has some unknown fields for it's address information.");
      emailService.sendErrorEmail("ERROR: Venue has some unknown address information",
              "<p>Venue with id " + venue.getId() + " has some unknown address information.</p>");
    }

    return venueRepository.save(venue);
  }

  public Venue getVenue(Long id) {
    Optional<Venue> venue = venueRepository.findById(id);
    if (venue.isEmpty()) {
      throw new VenueNotFound("Venue with id " + id + " not found.");
    }

    return venue.get();
  }
}
