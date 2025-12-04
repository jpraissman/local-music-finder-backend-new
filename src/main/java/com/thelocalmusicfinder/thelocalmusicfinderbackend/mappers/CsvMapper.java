package com.thelocalmusicfinder.thelocalmusicfinderbackend.mappers;

import com.thelocalmusicfinder.thelocalmusicfinderbackend.domain.band.Genre;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Band;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Event;
import com.thelocalmusicfinder.thelocalmusicfinderbackend.models.Venue;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class CsvMapper {

  public String toEventsCsv(List<Event> events) {
    StringWriter stringWriter = new StringWriter();

    CsvWriterSettings csvSettings = new CsvWriterSettings();
    csvSettings.setQuoteAllFields(true);

    CsvWriter writer = new CsvWriter(stringWriter, csvSettings);
    writer.writeHeaders(
            "eventId",
            "eventCode",
            "bandId",
            "bandName",
            "bandType",
            "tributeBandName",
            "bandGenres",
            "hasVideo",
            "venueId",
            "venueName",
            "address",
            "town",
            "county",
            "createdDate",
            "email",
            "eventDate",
            "startTime",
            "endTime",
            "coverCharge",
            "additionalInfo",
            "eventCreator");

    DateTimeFormatter dateDayFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    for (Event e : events) {
      ZoneId newYorkZone = ZoneId.of("America/New_York");
      ZonedDateTime createdAtInNYTime = e.getCreatedAt().atZone(newYorkZone);
      writer.writeRow(
              e.getId(),
              e.getEventCode(),
              e.getBand().getId(),
              e.getBand().getBandName(),
              e.getBand().getBandType(),
              e.getBand().getTributeBandName(),
              genresListToString(e.getBand().getGenres()),
              !e.getBand().getYoutubeVideoIds().isEmpty() ? "Yes" : "No",
              e.getVenue().getId(),
              e.getVenue().getVenueName(),
              e.getVenue().getLocation().getFormattedAddress(),
              e.getVenue().getLocation().getTown(),
              e.getVenue().getLocation().getCounty(),
              createdAtInNYTime.format(dateDayFormatter),
              e.getEmail(),
              e.getEventDate(),
              e.getStartTime(),
              e.getEndTime(),
              e.getCoverCharge(),
              e.getAdditionalInfo(),
              e.getEventCreator()
      );
    }

    writer.flush();

    return stringWriter.toString();
  }

  public String toBandsCsv(List<Band> bands) {
    StringWriter stringWriter = new StringWriter();

    CsvWriterSettings csvSettings = new CsvWriterSettings();
    csvSettings.setQuoteAllFields(true);

    CsvWriter writer = new CsvWriter(stringWriter, csvSettings);
    writer.writeHeaders(
            "bandId",
            "bandName",
            "bandType",
            "genres",
            "tributeBandName",
            "facebookUrl",
            "instagramUrl",
            "websiteUrl"
    );

    for (Band b : bands) {
      writer.writeRow(
              b.getId(),
              b.getBandName(),
              b.getBandType(),
              genresListToString(b.getGenres()),
              b.getTributeBandName(),
              b.getFacebookUrl(),
              b.getInstagramUrl(),
              b.getWebsiteUrl()
      );
    }

    writer.flush();

    return stringWriter.toString();
  }

  public String toVenuesCsv(List<Venue> venues) {
    StringWriter stringWriter = new StringWriter();

    CsvWriterSettings csvSettings = new CsvWriterSettings();
    csvSettings.setQuoteAllFields(true);

    CsvWriter writer = new CsvWriter(stringWriter, csvSettings);
    writer.writeHeaders(
            "venueId",
            "venueName",
            "address",
            "county",
            "town",
            "phoneNumber",
            "facebookUrl",
            "instagramUrl",
            "websiteUrl"
    );

    for (Venue v : venues) {
      writer.writeRow(
              v.getId(),
              v.getVenueName(),
              v.getLocation().getFormattedAddress(),
              v.getLocation().getCounty(),
              v.getLocation().getTown(),
              v.getPhoneNumber(),
              v.getFacebookUrl(),
              v.getInstagramUrl(),
              v.getWebsiteUrl()
      );
    }

    writer.flush();

    return stringWriter.toString();
  }

  private String genresListToString(List<Genre> genres) {
    StringBuilder result = new StringBuilder();
    for (Genre g : genres) {
      result.append(g.toString()).append(";");
    }
    return result.toString();
  }
}
