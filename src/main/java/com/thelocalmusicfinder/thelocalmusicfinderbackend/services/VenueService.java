package com.thelocalmusicfinder.thelocalmusicfinderbackend.services;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.venue.BasicVenueInfo;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.venue.VenueWithSimScore;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.venue.VenueDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.errors.exceptions.VenueNotFound;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers.VenueMapper;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Event;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Location;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Venue;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.repositories.EventRepository;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.repositories.VenueRepository;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.util.StringSimilarity;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VenueService {

  private final VenueRepository venueRepository;
  private final MapsService mapsService;
  private final LoggerService logger;
  private final EmailService emailService;
  private final VenueMapper  venueMapper;
  private final EventRepository  eventRepository;

  /**
   * @return the first 5 venues whose name contains the given venueNameQuery
   */
  public List<Venue> searchVenues(String venueNameQuery) {
    // Return first 5 venues that match
    List<Venue> matchingVenues = venueRepository.findByVenueNameContainingIgnoreCase(venueNameQuery);
    if (!matchingVenues.isEmpty()) {
      return matchingVenues.subList(0, Math.min(5,  matchingVenues.size()));
    }

    // If no venues match, return the 5 with the highest similarity score
    List<Venue> allVenues = venueRepository.findAll();
    List<VenueWithSimScore> similarVenues = new ArrayList<>();
    for (Venue venue: allVenues) {
      double simScore = StringSimilarity.findSimilarity(venueNameQuery, venue.getVenueName());
      if (simScore > 15 && similarVenues.size() < 5) {
        similarVenues.add(new VenueWithSimScore(venue, simScore));
      } else if (simScore > 15) {
        for (int i = 0; i < 5; i++) {
          if (simScore > similarVenues.get(i).getSimScore()) {
            similarVenues.add(i,  new VenueWithSimScore(venue, simScore));
            similarVenues.removeLast();
            break;
          }
        }
      }
    }

    List<Venue> result = new ArrayList<>();
    for (VenueWithSimScore venueWithSimScore : similarVenues) {
      result.add(venueWithSimScore.getVenue());
    }
    return result;
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
      if (simScore > 20) {
        logger.info("Venue with name " + venue.getVenueName() + " is potential duplicate to " + venueName + " with simScore of " + simScore);
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

  @Transactional
  public void mergeVenues(Long venue1Id, Long venue2Id, VenueDTO mergedVenueInfo) {
    Venue venue1 = this.getVenue(venue1Id);
    Venue venue2 = this.getVenue(venue2Id);

    for (Event venue2Event : venue2.getEvents()) {
      venue2Event.setVenue(venue1);
    }
    eventRepository.saveAll(venue2.getEvents());

    this.updateVenue(venue1, venueMapper.toBasicVenueInfo(mergedVenueInfo));

    venueRepository.delete(venue2);
  }
}
