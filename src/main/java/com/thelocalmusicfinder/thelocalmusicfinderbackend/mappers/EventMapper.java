package com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.band.BandDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.event.EventDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.event.UpsertEventRequestDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.dto.venue.VenueDTO;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Event;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventMapper {
  private final BandMapper bandMapper;
  private final VenueMapper venueMapper;

  public EventDTO toEventDTO(Event event) {
    BandDTO bandDTO = bandMapper.toBandDTO(event.getBand());
    VenueDTO venueDTO = venueMapper.toVenueDTO(event.getVenue());
    return EventDTO.builder()
            .id(event.getId())
            .venue(venueDTO)
            .band(bandDTO)
            .eventDate(event.getEventDate())
            .startTime(event.getStartTime())
            .endTime(event.getEndTime())
            .coverCharge(event.getCoverCharge())
            .additionalInfo(event.getAdditionalInfo()).build();
  }

  public UpsertEventRequestDTO toUpsertEventRequestDTO(Event event) {
    return UpsertEventRequestDTO.builder()
            .bandName(event.getBand().getBandName())
            .bandType(event.getBand().getBandType())
            .tributeBandName(event.getBand().getTributeBandName())
            .genres(event.getBand().getGenres())
            .venueName(event.getVenue().getVenueName())
            .locationId(event.getVenue().getLocation().getLocationId())
            .venueFacebookUrl(event.getVenue().getFacebookUrl())
            .venueInstagramUrl(event.getVenue().getInstagramUrl())
            .venueWebsiteUrl(event.getVenue().getWebsiteUrl())
            .bandFacebookUrl(event.getBand().getFacebookUrl())
            .bandInstagramUrl(event.getBand().getInstagramUrl())
            .bandWebsiteUrl(event.getBand().getWebsiteUrl())
            .venuePhone(event.getVenue().getPhoneNumber())
            .posterEmail(event.getEmail())
            .eventDate(event.getEventDate())
            .startTime(event.getStartTime())
            .endTime(event.getEndTime())
            .coverCharge(event.getCoverCharge())
            .additionalInfo(event.getAdditionalInfo())
            .eventCreator(event.getEventCreator())
            .agreesToTermsAndPrivacy(event.getAgreesToTermsAndPrivacy()).build();
  }
}
