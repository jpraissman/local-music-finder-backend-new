package com.thelocalmusicfinder.thelocalmusicfinderbackend.services;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.BasicVenueInfo;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.errors.exceptions.VenueNotFound;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Location;
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
    // Check for existing venue based on name and location
    String venueName = venueInfo.getVenueName().trim();
    Location venueLocation = mapsService.getLocationById(venueInfo.getLocationId());
    Optional<Venue> existingVenue = venueRepository.findByVenueNameAndLocation(venueName, venueLocation);

    if (existingVenue.isPresent()) {
      return this.updateVenue(existingVenue.get(), venueInfo);
    } else {
      // TODO: Check if there is a similar named venue or one with same address?? And send email
      return this.createVenue(venueInfo, venueLocation);
    }
  }

  private Venue updateVenue(Venue existingVenue, BasicVenueInfo updatedVenueInfo) {
    existingVenue.setFacebookUrl(updatedVenueInfo.getFacebookUrl());
    existingVenue.setInstagramUrl(updatedVenueInfo.getInstagramUrl());
    existingVenue.setWebsiteUrl(updatedVenueInfo.getWebsiteUrl());
    existingVenue.setPhoneNumber(updatedVenueInfo.getPhoneNumber());

    return venueRepository.save(existingVenue);
  }

  private Venue createVenue(BasicVenueInfo venueInfo, Location venueLocation) {
    Venue venue = Venue.builder()
            .venueName(venueInfo.getVenueName().trim())
            .location(venueLocation)
            .facebookUrl(venueInfo.getFacebookUrl())
            .instagramUrl(venueInfo.getInstagramUrl())
            .websiteUrl(venueInfo.getWebsiteUrl())
            .phoneNumber(venueInfo.getPhoneNumber())
            .build();

    if (venueLocation.getCounty() == null || venueLocation.getTown() == null) {
      logger.error("Venue with id " + venue.getId() + " has location with id " + venueLocation.getLocationId() + " which has null values for either town or county.");
      emailService.sendErrorEmail("ERROR: Venue has some unknown address information", "<p>Venue with id " + venue.getId() + " has some unknown address information.</p>");
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
