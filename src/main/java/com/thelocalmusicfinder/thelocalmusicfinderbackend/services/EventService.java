package com.thelocalmusicfinder.thelocalmusicfinderbackend.services;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.BasicVenueInfo;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.EventCreatorType;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.band.BasicBandInfo;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.UpsertEventRequestDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers.BandMapper;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers.VenueMapper;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Band;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Event;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Venue;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.repositories.EventRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {
  @Value("${FROM_EMAIL}")
  private String mainEmail;

  private final BandService bandService;
  private final VenueService venueService;
  private final BandMapper bandMapper;
  private final VenueMapper venueMapper;
  private final EventRepository eventRepository;
  private final EmailService emailService;

  private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  private static final SecureRandom random = new SecureRandom();

  /**
   * @return eventCode of the created event
   */
  @Transactional
  public String createEvent(UpsertEventRequestDTO payload) {
    // Upsert band
    BasicBandInfo bandInfo = bandMapper.toBasicBand(payload);
    Band band = bandService.upsertBand(bandInfo,
            payload.getEventCreator() == EventCreatorType.BAND);

    // Upsert venue
    BasicVenueInfo venueInfo = venueMapper.toBasicVenueInfo(payload);
    Venue venue = venueService.upsertVenue(venueInfo,
            payload.getEventCreator() == EventCreatorType.VENUE);

    // Create event
    String eventCode = this.getNewEventCode(8);
    Event event = Event.builder()
            .eventCode(eventCode)
            .band(band)
            .venue(venue)
            .email(payload.getPosterEmail())
            .eventDate(payload.getEventDate())
            .startTime(payload.getStartTime())
            .endTime(payload.getEndTime())
            .coverCharge(payload.getCoverCharge())
            .additionalInfo(payload.getAdditionalInfo())
            .eventCreator(payload.getEventCreator())
            .agreesToTermsAndPrivacy(payload.isAgreesToTermsAndPrivacy())
            .build();
    eventRepository.save(event);

    // Send event confirmation email and admin email
    if (!event.getEmail().equals(mainEmail)) {
      emailService.sendEventConfirmationEmail(event);
      emailService.sendAdminEventCreatedEmail(event);
    }

    // Check for duplicates
    List<Event> duplicateEvents = eventRepository.findByVenueAndEventDate(event.getVenue(), event.getEventDate());
    if (duplicateEvents.size() > 1) {
      emailService.sendDuplicateEventsEmail(duplicateEvents);
    }

    return event.getEventCode();
  }

//  @Transactional
//  public void editEvent(UpsertEventRequestDTO payload, String eventCode) {
//    // Make sure event exists
//    Optional<Event> existingEvent = eventRepository.findByEventCode(eventCode);
//    if (existingEvent.isEmpty()) {
//      // TODO: fix exception type
//      throw new NoSuchElementException("Event with eventCode " + eventCode + " not found");
//    }
//
//    // Upsert band
//    BasicBandInfo bandInfo = bandMapper.toBasicBand(payload);
//    Band band = bandService.upsertBand(bandInfo,
//            payload.getEventCreator() == EventCreatorType.BAND);
//
//    // Upsert venue
//    BasicVenueInfo venueInfo = venueMapper.toBasicVenueInfo(payload);
//    Venue venue = venueService.upsertVenue(venueInfo,
//            payload.getEventCreator() == EventCreatorType.VENUE);
//
//    // Update event
//    Event event = existingEvent.get();
//    event.setBand(band);
//    event.setVenue(venue);
//    event.setEventDate(payload.getEventDate());
//    event.setStartTime(payload.getStartTime());
//    event.setEndTime(payload.getEndTime());
//    event.setCoverCharge(payload.getCoverCharge());
//    event.setAdditionalInfo(payload.getAdditionalInfo());
//    event.setEventCreator(payload.getEventCreator());
//    event.setAgreesToTermsAndPrivacy(payload.isAgreesToTermsAndPrivacy());
//    eventRepository.save(event);
//
//
//    // TODO: At the end, if new venue/band created, need to check if they should be deleted or not
//  }

  private String getNewEventCode(int length) {
    String newEventCode = generateRandomString(length);
    while (eventRepository.findByEventCode(newEventCode).isPresent()) {
      newEventCode = generateRandomString(length);
    }
    return newEventCode;
  }

  private String generateRandomString(int length) {
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      int index = random.nextInt(CHARACTERS.length());
      sb.append(CHARACTERS.charAt(index));
    }
    return sb.toString();
  }
}
