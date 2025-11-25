package com.thelocalmusicfinder.thelocalmusicfinderbackend.services;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.BasicVenueInfo;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.band.BasicBandInfo;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.event.EventDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.event.UpsertEventRequestDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.errors.exceptions.EventNotFound;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers.BandMapper;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers.EventMapper;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers.VenueMapper;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Band;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Event;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Venue;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.repositories.BandRepository;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.repositories.EventRepository;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.repositories.VenueRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {
  @Value("${FROM_EMAIL}")
  private String adminEmail;
  private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  private static final SecureRandom random = new SecureRandom();

  private final EventMapper eventMapper;
  private final BandService bandService;
  private final VenueService venueService;
  private final BandMapper bandMapper;
  private final VenueMapper venueMapper;
  private final EventRepository eventRepository;
  private final EmailService emailService;
  private final LoggerService logger;
  private final BandRepository bandRepository;
  private final VenueRepository venueRepository;
  private final MapsService mapsService;

  /**
   * @return eventCode of the created event
   */
  @Transactional
  public String createEvent(UpsertEventRequestDTO payload) {
    logger.info("Creating new event. Event info: " + payload.toString());

    // Upsert band
    BasicBandInfo bandInfo = bandMapper.toBasicBand(payload);
    Band band = bandService.upsertBand(bandInfo);

    // Upsert venue
    BasicVenueInfo venueInfo = venueMapper.toBasicVenueInfo(payload);
    Venue venue = venueService.upsertVenue(venueInfo);

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
    if (!event.getEmail().equals(adminEmail)) {
      emailService.sendEventConfirmationEmail(event);
      emailService.sendAdminEventUpsertedEmail(event, true);
    }

    // Check for duplicates
    List<Event> duplicateEvents = eventRepository.findByVenueAndEventDate(event.getVenue(), event.getEventDate());
    if (duplicateEvents.size() > 1) {
      emailService.sendDuplicateEventsEmail(duplicateEvents);
    }

    return event.getEventCode();
  }

  @Transactional
  public void editEvent(UpsertEventRequestDTO payload, String eventCode) {
    // Make sure event exists
    Optional<Event> existingEvent = eventRepository.findByEventCode(eventCode);
    if (existingEvent.isEmpty()) {
      throw new EventNotFound("Event with eventCode " + eventCode + " not found");
    }

    // Upsert band
    BasicBandInfo bandInfo = bandMapper.toBasicBand(payload);
    Band band = bandService.upsertBand(bandInfo);

    // Upsert venue
    BasicVenueInfo venueInfo = venueMapper.toBasicVenueInfo(payload);
    Venue venue = venueService.upsertVenue(venueInfo);

    // Update event
    Event event = existingEvent.get();
    event.setBand(band);
    event.setVenue(venue);
    event.setEventDate(payload.getEventDate());
    event.setStartTime(payload.getStartTime());
    event.setEndTime(payload.getEndTime());
    event.setCoverCharge(payload.getCoverCharge());
    event.setAdditionalInfo(payload.getAdditionalInfo());
    event.setEventCreator(payload.getEventCreator());
    event.setAgreesToTermsAndPrivacy(payload.isAgreesToTermsAndPrivacy());
    eventRepository.save(event);

    // Send admin email
    if (!event.getEmail().equals(adminEmail)) {
      emailService.sendAdminEventUpsertedEmail(event, false);
    }

    // Check for duplicates
    List<Event> duplicateEvents = eventRepository.findByVenueAndEventDate(event.getVenue(), event.getEventDate());
    if (duplicateEvents.size() > 1) {
      emailService.sendDuplicateEventsEmail(duplicateEvents);
    }

    // Delete any venues or bands that have 0 events
    bandRepository.deleteAllWithNoEvents();
    venueRepository.deleteAllWithNoEvents();
  }

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

  /**
   * Finds all events within {distance} miles of the given location.
   * @param locationId locationId to search for events in
   * @param distance distance from location (in miles)
   * @param timezone timezone to search in
   */
  @Transactional
  public List<EventDTO> findEvents(String locationId, int distance, String timezone) {
    LocalDate todayDate = getTodayDate(timezone);
    List<Event> potentialEvents = eventRepository.findByEventDateGreaterThanEqual(todayDate);
    return mapsService.filterEventsByDistance(potentialEvents, locationId, distance);
  }

  public List<EventDTO> getEventsNextSevenDays(String timezone) {
    LocalDate todayDate = getTodayDate(timezone);
    LocalDate sevenDaysAfterToday = todayDate.plusDays(7);
    List<Event> events = eventRepository.findByEventDateBetween(todayDate, sevenDaysAfterToday);
    return eventMapper.toEventDTOs(events);
  }

  public List<EventDTO> getEventsByCounty(String county, String timezone, int numDays) {
    LocalDate todayDate = getTodayDate(timezone);
    LocalDate thirtyDaysAfterToday = todayDate.plusDays(numDays);
    List<Event> events = eventRepository.findByEventDateBetweenAndVenue_Location_County(todayDate, thirtyDaysAfterToday, county);
    return eventMapper.toEventDTOs(events);
  }

  public List<EventDTO> getEventsByIds(List<Long> ids) {
    List<Event> events = eventRepository.findByIdIn(ids);
    return eventMapper.toEventDTOs(events);
  }

  public Event getEvent(String eventCode) {
    Optional<Event> event =  eventRepository.findByEventCode(eventCode);
    if (event.isEmpty()) {
      throw new EventNotFound("Event with eventCode " + eventCode + " not found");
    }
    return event.get();
  }

  @Transactional
  public void deleteEvent(String eventCode) {
    eventRepository.deleteByEventCode(eventCode);

    // Delete any venues or bands that have 0 events
    bandRepository.deleteAllWithNoEvents();
    venueRepository.deleteAllWithNoEvents();
  }

  private LocalDate getTodayDate(String timezone) {
    ZoneId zone = ZoneId.of(timezone);
    ZonedDateTime zonedNow = ZonedDateTime.now(zone);
    return zonedNow.toLocalDate();
  }
}
