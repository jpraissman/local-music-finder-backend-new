package com.thelocalmusicfinder.thelocalmusicfinderbackend.services;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.BasicVenueInfo;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.errors.exceptions.VenueNotFound;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers.VenueMapper;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Location;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Venue;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.repositories.VenueRepository;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.util.StringSimilarity;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
  private final VenueMapper  venueMapper;

  public List<Venue> getAllVenues() {
    return venueRepository.findAll();
  }

  /**
   * Creates or updates a venue based on the given information. Matches for
   * existing venues based on locationId
   * @return the created or updated venue
   */
  public Venue upsertVenue(BasicVenueInfo venueInfo) {
    // Check for existing venue based on location id
    Optional<Venue> existingVenue = venueRepository.findByLocation_LocationId(venueInfo.getLocationId());

    if (existingVenue.isPresent()) {
      return this.updateVenue(existingVenue.get(), venueInfo);
    } else {
      return this.createVenue(venueInfo);
    }
  }

  public Venue updateVenue(Venue existingVenue, BasicVenueInfo updatedVenueInfo) {
    emailService.sendVenueUpdatedEmail(venueMapper.toBasicVenueInfo(existingVenue), updatedVenueInfo, existingVenue.getId());

    Location newLocation = mapsService.getLocationById(updatedVenueInfo.getLocationId());
    String newVenueName = updatedVenueInfo.getVenueName().trim();

    existingVenue.setFacebookUrl(updatedVenueInfo.getFacebookUrl());
    existingVenue.setInstagramUrl(updatedVenueInfo.getInstagramUrl());
    existingVenue.setWebsiteUrl(updatedVenueInfo.getWebsiteUrl());
    existingVenue.setPhoneNumber(updatedVenueInfo.getPhoneNumber());
    existingVenue.setLocation(newLocation);
    existingVenue.setVenueName(newVenueName);

    Venue savedVenue = venueRepository.save(existingVenue);

    validateLocation(savedVenue.getId(), newLocation);
    checkForDuplicates(newVenueName);

    return savedVenue;
  }

  private Venue createVenue(BasicVenueInfo venueInfo) {
    Location location = mapsService.getLocationById(venueInfo.getLocationId());
    String venueName = venueInfo.getVenueName().trim();

    Venue venue = Venue.builder()
            .venueName(venueName)
            .location(location)
            .facebookUrl(venueInfo.getFacebookUrl())
            .instagramUrl(venueInfo.getInstagramUrl())
            .websiteUrl(venueInfo.getWebsiteUrl())
            .phoneNumber(venueInfo.getPhoneNumber())
            .build();

    Venue savedVenue = venueRepository.save(venue);

    validateLocation(savedVenue.getId(), location);
    checkForDuplicates(venueName);

    return savedVenue;
  }

  private void validateLocation(Long venueId, Location location) {
    if (location.getCounty() == null || location.getTown() == null) {
      logger.error("Venue with id " + venueId + " has location with id " + location.getLocationId() + " which has null values for either town or county.");
      emailService.sendErrorEmail("ERROR: Venue has some unknown address information", "<p>Venue with id " + venueId + " has some unknown address information.</p>");
    }
  }

  private void checkForDuplicates(String venueName) {
    List<Venue> allVenues = venueRepository.findAll();
    List<Venue> potentialDuplicateVenues = new ArrayList<>();

    for (Venue venue : allVenues) {
      double simScore = StringSimilarity.findSimilarity(venueName, venue.getVenueName());
      if (simScore > 10) {
        potentialDuplicateVenues.add(venue);
      }
    }

    if (potentialDuplicateVenues.size() > 1) {
      logger.warn("Found " + potentialDuplicateVenues.size() + " venues with similar names");
      for (Venue venue : potentialDuplicateVenues) {
        logger.warn("Similarity: Venue id " + venue.getId() + ": " + venue.getVenueName());
      }
      emailService.sendDuplicateVenueEmail(potentialDuplicateVenues);
    }
  }

  public Venue getVenue(Long id) {
    Optional<Venue> venue = venueRepository.findById(id);
    if (venue.isEmpty()) {
      throw new VenueNotFound("Venue with id " + id + " not found.");
    }

    return venue.get();
  }
}
